package org.backend.server.microservices.authorization.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final BytesKeyGenerator bytesKeyGenerator;
    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public JwtUtil(BytesKeyGenerator bytesKeyGenerator) {
        this.bytesKeyGenerator = bytesKeyGenerator;
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Jws<?> extractToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid JWT signature", e);
        }
    }

    public String getClaim(String token, String claimName) {
        Claims claims = (Claims) extractToken(token);
        return claims.get(claimName, String.class);  // Get a specific claim by name
    }

    public Claims getClaims(Jws<?> token) {
        return (Claims) token.getBody();
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            String subject
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String buildToken(String subject, String... accessLevel) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("TOKEN_ID", bytesKeyGenerator.generateKey());
        claims.put("ACCESS_LEVEL", accessLevel);
        return buildToken(claims, subject);
    }

    public String buildTokenWithUsername(String username, String subject, String... accessLevel) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("TOKEN_ID", bytesKeyGenerator.generateKey());
        claims.put("ACCESS_LEVEL", accessLevel);
        claims.put("USER_NAME", username);
        return buildToken(claims, subject);
    }
}
