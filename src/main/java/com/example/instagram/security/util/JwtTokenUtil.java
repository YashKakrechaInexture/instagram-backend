package com.example.instagram.security.util;

import com.example.instagram.security.dto.JwtUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    @Serial
    private static final long serialVersionUID = -2550185165626007488L;

    public static final String JWT_ROLE = "role";

    public static final String JWT_ID = "id";

    public static final String JWT_USERNAME = "username";

    @Value("${jwt.token.validity}")
    private long jwtTokenValidity;

    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(JwtUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JWT_ROLE, userDetails.isAdmin());
        claims.put(JWT_ID, userDetails.getId());
        claims.put(JWT_USERNAME, userDetails.getActualUsername());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        JwtUserDetails jwtUserDetails = (JwtUserDetails) userDetails;
        final String username = getUsernameFromToken(token);
        final boolean role = Boolean.parseBoolean(getClaimFromToken(token, claims -> claims.get(JWT_ROLE)).toString());
        if(role != jwtUserDetails.isAdmin()){
            throw new AccessDeniedException("Your permissions have been changed. Please log in again.");
        }
        return (username.equals(jwtUserDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractClaimValue(String token, String claim) {
        return this.getClaimFromToken(token.substring(7), claims -> claims.get(claim)).toString();
    }
}