package com.wooreal.gravitygather.utils;

import com.wooreal.gravitygather.exception.BusinessLogicException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    private String SECRET_KEY;

    @Value("${spring.jwt.explain.access}")
    private long ACCESS_EXPLAIN;

    @Value("${spring.jwt.explain.refresh}")
    private long REFRESH_EXPLAIN;

    SecretKey secret;

    public JwtTokenUtil(@Value("${spring.jwt.secretkey}") String SECRET_KEY){
        String salt = SHA256Util.generateSalt();
        String hash = SHA256Util.generateHashWithSalt(SECRET_KEY, salt);
        this.SECRET_KEY = hash;
        this.secret = Keys.hmacShaKeyFor(hash.getBytes());
    }

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

    public Integer getUserSeqFromHttpServletRequest(HttpServletRequest request){
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);
        return getUserSeqFromToken(jwtToken);
    }

    public Integer getUserSeqFromToken(String token){
        return Integer.parseInt(extractAllClaims(token).getSubject());
    }

    public Integer getUserSeqFromSecurityContext(){
        try {
            Integer seq = (Integer) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
            return seq;
        }catch (Exception e){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "권한이 없습니다.");
        }
    }
}
