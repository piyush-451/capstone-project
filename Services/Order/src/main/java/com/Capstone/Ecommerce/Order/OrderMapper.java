package com.Capstone.Ecommerce.Order;

import com.Capstone.Ecommerce.OrderLine.OrderLineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderLineMapper orderLineMapper;

    public OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getPaymentMethod()
        );
    }

    public Order toOrder(OrderRequest request) {
        Order order =  Order.builder()
                .paymentMethod(request.paymentMethod())
                .orderLines(request.orderLine().stream()
                        .map(orderLineMapper::toOrderLine)
                        .collect(Collectors.toList()))
                .build();

        order.getOrderLines()
                .forEach(orderLine -> orderLine.setOrder(order));

        return order;
    }
}
