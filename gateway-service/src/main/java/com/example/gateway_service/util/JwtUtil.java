package com.example.gateway_service.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    @Value("${jwt.secret:123}")
    private String secretKey;


    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    private Key getSignKey() {
        // Chuyển đổi khóa bí mật từ BASE64 nếu cần và đảm bảo đủ độ dài
        byte[] keyBytes = secretKey.getBytes(); // Nếu secretKey đã là BASE64, bạn có thể giải mã tại đây
        return Keys.hmacShaKeyFor(keyBytes);  // Sử dụng khóa này cho HS256
    }

    public String extractClaim(String token, String claimKey) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(claimKey, String.class);
    }

}