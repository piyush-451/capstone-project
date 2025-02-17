package com.Capstone.Ecommerce.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${security.jwt.secretKey}")
    private String secretKey;



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


    private SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
