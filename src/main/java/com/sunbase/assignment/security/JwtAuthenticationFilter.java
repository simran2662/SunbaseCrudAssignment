package com.sunbase.assignment.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private JwtAuthToken jwtAuthToken;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    //when we call any api it will validate from here 
    //validating username 
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get the authorization header from the request
        String requestHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        logger.info("Processing authentication for '{}'", request.getRequestURI());

        // Check if the authorization header is present and starts with "Bearer "
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.substring(7); // Extract the JWT token
            try {
                username = jwtAuthToken.getUsernameFromToken(token); // Extract username from JWT token
            } catch (IllegalArgumentException e) {
                logger.error("Illegal argument while fetching username from token");
            } catch (ExpiredJwtException e) {
                logger.error("JWT token has expired");
            } catch (MalformedJwtException e) {
                logger.error("JWT token is malformed");
            } catch (NullPointerException e) {
                logger.error("Reference jwtAuthToken is null");
            } catch (SignatureException e) {
                logger.error("JWT token not matched");
            } catch (Exception e) {
                logger.error("An error occurred while processing JWT token");
            }
        }

        // If username is retrieved from token and user not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details from userDetailsService
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            
            // Validate the JWT token
            if (jwtAuthToken.validateToken(token, userDetails)) {
                // Create authentication object
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Set authentication in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("Authenticated user '{}', setting security context", username);
            } else {
                logger.warn("Token validation failed for user '{}'", username);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
