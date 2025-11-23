package uz.ems.maydon24.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final String SECRET = "wgZLShDJ98aQVgQFtdQ0+sYrV5zqK6+fJiOLtU1YZKxYEdUghMFq93sdUNhU+aWoJlD80I5gKn+Rz8UhDZQhzA==";

    private final UserDetailsService userDetailsService;

    public JwtUtil(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        claims.put("roles", userDetails.getAuthorities());
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean validateToken(String token, String username) {
        return (extractUsername(token).equals(username) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}

//
//@Component
//public class JwtUtil {
//
//    private static final String KEY = "wgZLShDJ98aQVgQFtdQ0+sYrV5zqK6+fJiOLtU1YZKxYEdUghMFq93sdUNhU+aWoJlD80I5gKn+Rz8UhDZQhzA==";
//    private final String SECRET_KEY2 = Base64.getEncoder().encodeToString(KEY.getBytes());
//
//    private final UserDetailsService userDetailsService;
//
//    public JwtUtil(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    public String generateToken(String username) {
//        Map<String, Object> claims = new HashMap<>();
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        var roles = userDetails.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toSet())
//                .toString();
//        claims.put("roles", roles);
//        return createToken(claims, username);
//    }
//
//    private String createToken(Map<String, Object> claims, String subject) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1 kun
//                .signWith(SignatureAlgorithm.HS512, SECRET_KEY2)
//                .compact();
//    }
//
//    public Boolean validateToken(String token, String username) {
//        final String extractedUsername = extractUsername(token);
//        return (extractedUsername.equals(username) && !isTokenExpired(token));
//    }
//
//    public String extractUsername(String token) {
//        return extractAllClaims(token).getSubject();
//    }
//
//    public Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(SECRET_KEY2)
//                .setAllowedClockSkewSeconds(30)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public Boolean isTokenExpired(String token) {
//        return extractAllClaims(token).getExpiration().before(new Date());
//    }
//
//    public Date getExpiredDate(String token) {
//        return extractAllClaims(token).getExpiration();
//    }
//
//
//    public String getUsernameFromToken(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(getSigningKey2())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        return claims.getSubject();
//    }
//
//    private Key getSigningKey2() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY2);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//}
