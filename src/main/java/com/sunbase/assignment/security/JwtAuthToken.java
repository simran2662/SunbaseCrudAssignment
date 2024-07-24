package com.sunbase.assignment.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
//we are generating Token, validating Token etc.
@Component
@Service
public class JwtAuthToken {

    // Static key initialization for demonstration purposes
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Generates a JWT token based on user details.
     * 
     * @param userDetails The UserDetails object representing the user.
     * @return Generated JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities",
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Creates a JWT token with specified claims and subject (username).
     * 
     * @param claims   Claims to be included in the token.
     * @param userName Subject (username) for whom the token is created.
     * @return Created JWT token.
     */
    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60)) // Token expires in 30 minutes
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Retrieves a specific claim from the JWT token.
     * 
     * @param token          JWT token.
     * @param claimsResolver Function to resolve the specific claim from Claims.
     * @return Specific claim value.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Retrieves all claims from the JWT token.
     * 
     * @param token JWT token.
     * @return All claims extracted from the token.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /**
     * Validates the JWT token based on the given UserDetails.
     * 
     * @param token       JWT token to be validated.
     * @param userDetails UserDetails object representing the user.
     * @return true if the token is valid for the user, false otherwise.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Retrieves the username from the JWT token.
     * 
     * @param token JWT token.
     * @return Username extracted from the token.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Checks if the JWT token is expired.
     * 
     * @param token JWT token.
     * @return true if the token is expired, false otherwise.
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Retrieves the expiration date of the JWT token.
     * 
     * @param token JWT token.
     * @return Expiration date of the token.
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
}
