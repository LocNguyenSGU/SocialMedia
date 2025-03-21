package com.example.social.media.service.Impl;

import com.example.social.media.entity.InvalidatedToken;
import com.example.social.media.payload.request.AuthDTO.LogoutRequest;
import com.example.social.media.repository.InvalidatedRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtService {

    @Autowired
    InvalidatedRepository invalidatedRepository;

    // Replace this with a secure key in a real application, ideally fetched from environment variables
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    // Generate token with given user name
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .collect(Collectors.toList());

        claims.put("roles", roles);

        return createToken(claims , userDetails.getUsername());
    }

    // Create a JWT token with specified claims and subject (user name)
    private String createToken(Map<String, Object> claims, String userName) {
        claims.put("jti", UUID.randomUUID().toString());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // Token valid for 30 minutes
                .signWith(SignatureAlgorithm.HS256, getSignKey())
                .compact();
    }

    public void logout(LogoutRequest request) {

        String jit = extractJti(request.getToken());
        Date expiryTime = extractExpiration(request.getToken());

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedRepository.save(invalidatedToken);
    }

    public String extractJti(String token) {
        return extractClaim(token, Claims::getId); // Claims::getId tương ứng với "jti"
    }

    // Get the signing key for JWT token
    public Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract the username from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public List<String> extractRoles(String token) {
        Claims claims = Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
        return claims.get("roles", List.class);
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate the token against user details and expiration
    public Boolean validateToken(String token) {
        try {
            // Extract claims to verify token structure and signature
            Claims claims = extractAllClaims(token);

            // Check if token is expired
            if (isTokenExpired(token)) {
                return false;
            }

            // Extract jti from token
            String jti = claims.getId();

            // Check if token exists in invalidated_token repository
            Optional<InvalidatedToken> invalidatedToken = invalidatedRepository.findById(jti);
            if (invalidatedToken.isPresent()) {
                // Token has been invalidated (e.g., logged out)
                return false;
            }

            // Token is valid if it passes all checks
            return true;

        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            // Token không hợp lệ (sai định dạng, chữ ký không khớp, v.v.)
            return false;
        }
    }
}