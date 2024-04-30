package com.wak.game.global.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


@Slf4j
@Component
public class JWTUtils {

    public static final long ACCESS_TOKEN_EXPIRE_TIME =  14 * 24 * 60 * 60 * 1000; // 14일
    private final Key key;


    public JWTUtils(@Value("${spring.jwt.secret}") String secretKey){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createJwt(long user_id) {
        Claims claims = Jwts.claims();
        claims.put("user_id", user_id);
        long now = (new Date().getTime());

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        return token;
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
