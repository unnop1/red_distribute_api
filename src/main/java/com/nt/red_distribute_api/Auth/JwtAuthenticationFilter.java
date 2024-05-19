package com.nt.red_distribute_api.Auth;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.red_distribute_api.entity.UserEntity;
import com.nt.red_distribute_api.service.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;


    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        //Authorization

        String requestHeader = request.getHeader("Authorization");
        //Bearer 2352345235sdfrsfgsdfsdf
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
