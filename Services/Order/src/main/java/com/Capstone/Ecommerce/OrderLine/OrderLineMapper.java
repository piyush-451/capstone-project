package com.Capstone.Ecommerce.OrderLine;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLineMapper {
    public OrderLine toOrderLine(OrderLineRequest request){
        return OrderLine.builder()
                .productId(request.id())
                .quantity(request.quantity())
                .build();
    }
}
