package com.Capstone.Ecommerce.Jwt;

import com.Capstone.Ecommerce.Role.Role;
import com.Capstone.Ecommerce.User.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${security.jwt.secretKey}")
    private String secretKey;

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        Set<String> roles = user.getRoles().stream()
                        .map(Role::getRolename)
                        .collect(Collectors.toSet());

        claims.put("roles" , roles);
        claims.put("id",user.getId());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*60*60))
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims validateTokenAndGetClaims(String token){
        Claims claims =  extractAllClaims(token);
        if(isTokenExpired(claims.getExpiration())){
            throw new RuntimeException("token has expired");
        }
        return claims;
    }

    private boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
