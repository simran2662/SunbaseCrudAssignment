package com.sunbase.assignment.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
//whenever as exception is thrown
//due to unauthenticated user trying to access the resource that required authentication.
@Component
public class JwtAuthentication implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        // Set HTTP status code for unauthorized access 401 expire or wrong
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        // Set content type of the response
        response.setContentType("application/json");
        
        // Write custom error message to the response
        response.getWriter().write(request.getRequestURI() + " - " + authException.getMessage());
    }
}
