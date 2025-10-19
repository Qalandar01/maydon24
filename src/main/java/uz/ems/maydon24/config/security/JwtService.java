package uz.ems.maydon24.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.ems.maydon24.models.entity.Role;
import uz.ems.maydon24.models.entity.User;
import uz.ems.maydon24.models.enums.RoleName;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.token.lifetime.days}")
    private Integer lifetimeInDays;

    private long lifetimeInMillis;

    @PostConstruct
    public void init() {
        this.lifetimeInMillis = 1000L * 60 * 60 * 24 * lifetimeInDays;
        log.info("JWT lifetime (ms): {}", lifetimeInMillis);
    }

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public Boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Validation failed!", e);
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(User user) {
        long expirationMillis = System.currentTimeMillis() + lifetimeInMillis;

        String roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.joining(", "));

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("fullName", user.getFullName())
                .claim("phone", user.getPhone())
                .claim("visibility", user.isEnabled())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(expirationMillis))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public User getUserObject(String token) {
        Claims claims = getClaims(token);

        Long id = Long.valueOf(claims.getSubject());
        String fullName = claims.get("fullName", String.class);
        String phone = claims.get("phone", String.class);
        Boolean visibility = claims.get("visibility", Boolean.class);
        String roles = (String) claims.get("roles");
        Set<Role> authorities = Arrays.stream(roles.split(","))
                .map(role -> Role.builder()
                        .name(RoleName.valueOf(role.trim()))
                        .build())
                .collect(Collectors.toSet());

        return User.builder()
                .id(id)
                .phone(phone)
                .fullName(fullName)
                .visibility(visibility)
                .roles(authorities)
                .build();
    }

    public void setJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("m24-token", token);
        cookie.setHttpOnly(true);
        cookie.setDomain("maydon24");
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

    public String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("m24-token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
