package com.Capstone.Ecommerce.Kafka;

import com.Capstone.Ecommerce.Product.ProductRequest;
import com.Capstone.Ecommerce.Product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductConsumer {
    private final ProductService productService;

    @KafkaListener(topics = "order-topic", groupId = "orderGroup")
    public void consumeOrderLineConfirmation(KafkaProductRequest request){
        System.out.println("kafka consumer in product service");
        productService.updateProductById(
                request.id(),
                new ProductRequest(
                        null,
                        null,
                        request.availableQuantity(),
                        null,
                        null
                )
        );
    }
}
