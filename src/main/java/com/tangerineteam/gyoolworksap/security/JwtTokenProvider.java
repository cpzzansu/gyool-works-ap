package com.tangerineteam.gyoolworksap.security;

import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;

public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    @Value("${app.refresh-token-expiration-milliseconds}")
    private long jwtRefreshExpirationMs;

    // generate JWT token
//    public String generateToken(Member member) {
//        String memberId = member.getId();
//        Role role = member.getRole();
//
//        Date currentDate = new Date();
//
//        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
//
//        String token = Jwts.builder()
//                .setSubject(memberId)
//                .claim("role", role.name())
//                .setIssuedAt(currentDate)
//                .setExpiration(expireDate)
//                .signWith(key(), SignatureAlgorithm.HS256)
//                .compact();
//
//        return token;
//    }
//
//    public String generateRefreshToken(Member member) {
//        String memberId = member.getId();
//        Role role = member.getRole();
//
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + jwtRefreshExpirationMs);
//
//        return Jwts.builder()
//                .setSubject(memberId)
//                .claim("role", role)
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(key(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    private Key key() {
//        return Keys.hmacShaKeyFor(
//                Decoders.BASE64.decode(jwtSecret)
//        );
//    }
//
//    // get username from Jwt token
//    public String getUsername(String token) {
//
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        String username = claims.getSubject();
//
//        return username;
//    }
//
//    public String extractRole(String token) {
//        Claims claims = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
//        return claims.get("role").toString();
//    }
//
//    public String getUserId(String token) {
//
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        String userId = claims.getSubject();
//
//        return userId;
//    }
//
//    public boolean validateToken(String token) {
//        log.debug("Validating token {}", token);
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(key())
//                    .build()
//                    .parse(token);
//            log.debug("Token validated");
//            return true;
//        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
//            throw new APIException(HttpStatus.UNAUTHORIZED, ex.getMessage());
//        }
//    }
//
//    public String extractTokenFromCookie(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("Authorization")) {
//                    String token = cookie.getValue();
//                    if (token.startsWith("Bearer")) {
//                        return token.substring(6); // "Bearer "를 제거하고 실제 토큰만 반환
//                    }
//                    return token;
//                }
//            }
//        }
//        return null;
//    }
}
