package com.Capstone.Ecommerce.Config;

import com.Capstone.Ecommerce.Utils.UtilsFunctions;
import feign.Contract;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.Encoder;



@Configuration
@RequiredArgsConstructor
public class FeignClientConfig {
    private final UtilsFunctions utilsFunctions;

    @Bean
    public RequestInterceptor requestInterceptor(){
        return requestTemplate -> {
            // Get JWT from Security Context
            String jwt = utilsFunctions.getJwtToken();
            if (jwt != null) {
                requestTemplate.header("Authorization", "Bearer " + jwt);
            }
        };
    }

}
