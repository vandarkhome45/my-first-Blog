package com.example.blog.module.user.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 * 模块：用户认证模块
 * 
 * 功能：
 * - 生成 Token
 * - 解析 Token
 * - 验证 Token
 */
@Component
public class JwtUtil {

    /**
     * JWT 密钥（从配置文件读取）
     */
    @Value("${jwt.secret:MyBlogSystemSecretKey2024VeryLongSecretKeyForHS256}")
    private String secret;

    /**
     * Token 过期时间（默认 7 天，单位：毫秒）
     */
    @Value("${jwt.expiration:604800000}")
    private Long expiration;

    /**
     * 获取签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 从 Token 中提取用户名
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 从 Token 中提取用户 ID
     */
    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.get("userId", Integer.class);
    }

    /**
     * 从 Token 中提取角色
     */
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.get("role", String.class);
    }

    /**
     * 从 Token 中提取过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    /**
     * 生成 Token
     * 
     * @param userId 用户 ID
     * @param username 用户名
     * @param role 角色
     * @return JWT Token
     */
    public String generateToken(Integer userId, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 验证 Token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            System.err.println("❌ 无效的 JWT 签名");
        } catch (MalformedJwtException e) {
            System.err.println("❌ 无效的 JWT 格式");
        } catch (ExpiredJwtException e) {
            System.err.println("❌ JWT 已过期");
        } catch (UnsupportedJwtException e) {
            System.err.println("❌ 不支持的 JWT");
        } catch (IllegalArgumentException e) {
            System.err.println("❌ JWT claims 为空");
        }
        return false;
    }

    /**
     * 检查 Token 是否已过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expirationDate = getExpirationDateFromToken(token);
            return expirationDate.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * 刷新 Token（延长有效期）
     */
    public String refreshToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        String username = claims.getSubject();
        Integer userId = claims.get("userId", Integer.class);
        String role = claims.get("role", String.class);

        return generateToken(userId, username, role);
    }
}
