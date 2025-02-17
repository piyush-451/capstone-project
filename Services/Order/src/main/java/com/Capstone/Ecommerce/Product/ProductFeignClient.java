package com.Capstone.Ecommerce.Product;

import com.Capstone.Ecommerce.Config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(
        name = "product-service",
        url = "${feignClient.config.product-url}",
        configuration = FeignClientConfig.class
)
public interface ProductFeignClient {
    @GetMapping("/{id}")
    Optional<ProductResponse> getProductById(@PathVariable Long id);

//    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", consumes = "application/json")
    @PutMapping("/{id}")
    Optional<ProductResponse> updateProductById(
            @PathVariable Long id,
            @RequestBody ProductRequest request,
            @RequestHeader("API-KEY") String apiKey
            );


}
