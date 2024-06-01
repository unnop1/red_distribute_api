package com.nt.red_distribute_api.Auth;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.red_distribute_api.entity.UserEntity;
import com.nt.red_distribute_api.service.UserService;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private MeterRegistry meterRegistry;


    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Authorization
        Timer.Sample sample = Timer.start(meterRegistry);
        try{
            String requestHeader = request.getHeader("Authorization");
            //Bearer 2352345235sdfrsfgsdfsdf
            logger.info(" Header :  {}", request);
            String username = null;
            String token = null;
            if (requestHeader != null && requestHeader.startsWith("Bearer")) {

                //looking good
                token = requestHeader.substring(7);
                try {

                    username = this.jwtHelper.getUsernameFromToken(token);

                } catch (IllegalArgumentException e) {
                    logger.info("Illegal Argument while fetching the username !!");
                    e.printStackTrace();
                } catch (ExpiredJwtException e) {
                    logger.info("Given jwt token is expired !!");
                    e.printStackTrace();
                    JwtExpiredHandler(e.getMessage(), request, response);
                    return;
                } catch (MalformedJwtException e) {
                    logger.info("Some changed has done in token !! Invalid Token");
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();

                }


            }
            


            //
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                //fetch user detail from username
                UserEntity userDetails = this.userService.loadUserByUsername(username);
                Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
                if (validateToken) {
                    //set the authentication
                    System.out.println(" validateToken userDetails:"+userDetails.toString());
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);


                } else {
                    logger.info("Validation fails !!");
                }


            }

            filterChain.doFilter(request, response); //doubt hai
            // Record success metric
            sample.stop(Timer.builder("http_request_duration_seconds")
                    .description("Request latency in seconds.")
                    .tags("status", String.valueOf(response.getStatus()))
                    .register(meterRegistry));
        } catch (Exception ex) {
            // Record failure metric
            sample.stop(Timer.builder("http_request_duration_seconds")
                    .description("Request latency in seconds.")
                    .tags("status", "error")
                    .register(meterRegistry));
        } finally {
            // Record response status count
            Counter.builder("http_response_status_count")
                .description("Count of HTTP response status codes.")
                .tags("status", String.valueOf(response.getStatus()))
                .register(meterRegistry)
                .increment();
        }


    }

    private void JwtExpiredHandler(String error, HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Create a JSON error response
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Authentication failed");
        errorResponse.put("message", error);

        // Convert the error response to JSON
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(errorResponse);

        // Set the content type to JSON
        response.setContentType("application/json");

        // Write the JSON response to the output stream
        PrintWriter writer = response.getWriter();
        writer.println(jsonResponse);
    }
}
