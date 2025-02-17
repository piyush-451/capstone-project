package com.Capstone.Ecommerce.Jwt;

import com.Capstone.Ecommerce.Utils.UtilsFunctions;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UtilsFunctions conversionUtils;

    @Value("${security.apiKey.apiSecret}")
    private String apiSecretKey;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try{
            System.out.println(request.getRequestURI());
            String token = getTokenFromHeader(request);
            if(token == null){
                System.out.println("token null");
                throw new AuthenticationCredentialsNotFoundException("Missing JWT Token");
            }
            System.out.println("still continue after token null");

            Claims claims = jwtService.validateTokenAndGetClaims(token);
            Map<String,String> userDetails = new HashMap<>();
            userDetails.put("id",String.valueOf(claims.get("id",Long.class)));
            userDetails.put("email", claims.getSubject());


            Set<String> roles = new HashSet<>(conversionUtils.extractListFromClaims(claims,"roles"));

            Set<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role->new SimpleGrantedAuthority("ROLE_"+role))
                    .collect(Collectors.toSet());

            String apiKey = request.getHeader("API-Key");
            if(apiKey != null && apiKey.equals(apiSecretKey)){
                authorities.add(new SimpleGrantedAuthority("ROLE_INTERNAL"));
            }

            if(SecurityContextHolder.getContext().getAuthentication() == null){
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,authorities);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            System.out.println("still continue after token validation throws error");

        }
        catch (Exception e){
            System.out.println(e.getMessage());
            request.setAttribute("jwtException",e);
        }

        filterChain.doFilter(request,response);
    }


    private String getTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        List<String> allowedPaths = List.of("/auth/", "/actuator/");
        String uri = request.getRequestURI();
        System.out.println("without authentication");
        return allowedPaths.stream().anyMatch(uri::startsWith);
    }
}
