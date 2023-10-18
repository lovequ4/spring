package com.example.dashboard.Security;


import javax.crypto.SecretKey;
import java.util.Date;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


public class JwtUtils {
    
    private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static final long expiration = 7200;

    public static String generateToken(Long Id , String name, String role){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        return Jwts.builder()
                .setSubject(name)
                .claim("id", Id)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public static class InvalidTokenException extends Exception {
        public InvalidTokenException(String message) {
            super(message);
        }
    }

    public static Claims parseToken(String token) throws InvalidTokenException {
        try {
            Jws<Claims> claimsJwt = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);

            return  claimsJwt.getBody();
        } catch (Exception e) {
           throw new InvalidTokenException("Invalid token");
        }
    }
}
