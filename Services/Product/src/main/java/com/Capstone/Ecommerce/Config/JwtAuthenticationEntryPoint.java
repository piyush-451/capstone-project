package com.Capstone.Ecommerce.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException
    ) throws IOException, ServletException {
        System.out.println("entry filter");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");


        Exception e = (Exception) request.getAttribute("jwtException");
        Map<String,String> errors = new HashMap<>();

        errors.put("Error Description","Unauthorized");
        if(e != null){
            System.out.println("catch block exception - " + e.getMessage());
            errors.put("message", e.getMessage());
        }
        else if(authException != null){
            System.out.println("why no authException " + authException.getMessage());
            errors.put("message",authException.getMessage());
        }
        else {
            errors.put("message","Authentication failure");
        }

        response.getWriter().write(new ObjectMapper().writeValueAsString(errors));
    }
}
