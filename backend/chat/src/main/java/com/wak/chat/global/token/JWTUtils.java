package com.wak.chat.global.token;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class JWTUtils {

    private final Key key;


    public JWTUtils(@Value("${spring.jwt.secret}") String secretKey){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Long getId(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token)
                .getBody().get("user_id", Long.class);
    }

    public boolean isExpired(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());
    }

}
