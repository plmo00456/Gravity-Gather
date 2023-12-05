package com.wooreal.gravitygather.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${spring.jwt.secretkey}")
    private String SECRET_KEY;

    @Value("${spring.jwt.explain.access}")
    private long ACCESS_EXPLAIN;

    @Value("${spring.jwt.explain.refresh}")
    private long REFRESH_EXPLAIN;

    SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(secret)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    // mode     0 : 액세스
    //          1 : 리프레시
    public String generateToken(int seq, int mode) {
        return Jwts.builder()
//            .claims(claims)   // 권한 등 추가적인 claim가 필요없음
            .subject(seq+"")
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * (mode == 0 ? ACCESS_EXPLAIN : 24 * REFRESH_EXPLAIN)))
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
            .compact();
    }

    // 0 : 성공
    // 1 : 본인 계정 아님, 권한 없음
    // 2 : 토큰 만료
    public int validateToken(String token, int seq) {
        try {
            Jwts.parser().verifyWith(secret).build().parseSignedClaims(token);
            return extractAllClaims(token).getSubject().equals(seq+"") ? 0 : 1;
        } catch (JwtException e) {
            return 2;
        }
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secret).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Integer getUserSeqFromToken(String token){
        return Integer.parseInt(extractAllClaims(token).getSubject());
    }
}
