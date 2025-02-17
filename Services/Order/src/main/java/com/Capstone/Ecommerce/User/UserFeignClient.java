package com.Capstone.Ecommerce.User;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "user-service",
        url = "${feignClient.config.user-url}"
)
public interface UserFeignClient {
    @GetMapping("/{id}")
    Optional<UserResponse> getUserById(@PathVariable Long id);
}
