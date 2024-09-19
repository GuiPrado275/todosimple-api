package com.guilhermepb.todosimple.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Component
public class JWTUtil {

    @Value("${jwt.secret}") //of application properties
    private String secret;

    @Value("${jwt.expiration}") //of application properties
    private Long expiration;

    public String generateToken(String username) { // token that in future will be expired
        SecretKey key = getKeyBySecret();
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + this.expiration)) //time of expiration
                .signWith(key)
                .compact();
    }


    private SecretKey getKeyBySecret() {                            //the key of encrypt
        SecretKey key = Keys.hmacShaKeyFor(this.secret.getBytes());
        return key;
    }

    public boolean isValidToken(String token) {   //to validate that this token is after now
        Claims claims = getClaims(token);
        if (Objects.nonNull(claims)) {
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());
            if (Objects.nonNull(username) && Objects.nonNull(expirationDate) && now.before(expirationDate)) {
                return true;
            }
        }
        return false;
    }

    private Claims getClaims(String token) { //For generate claims - transform token in dates, decrypt
        SecretKey key = getKeyBySecret();
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null; //if the object is null or invalid, return this
        }
    }

}
