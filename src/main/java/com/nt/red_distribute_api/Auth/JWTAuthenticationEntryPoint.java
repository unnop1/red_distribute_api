package com.nt.red_distribute_api.Auth;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Create a JSON error response
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Authentication failed");
        errorResponse.put("message", authException.getMessage());

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
