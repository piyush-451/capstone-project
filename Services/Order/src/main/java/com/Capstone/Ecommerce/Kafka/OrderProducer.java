package com.Capstone.Ecommerce.Kafka;

import com.Capstone.Ecommerce.Product.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate<String, ProductRequest> kafkaTemplate;

    public void sendOrderLineConfirmation(ProductRequest productRequest){
        Message<ProductRequest> message = MessageBuilder
                .withPayload(productRequest)
                .setHeader(KafkaHeaders.TOPIC,"order-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
