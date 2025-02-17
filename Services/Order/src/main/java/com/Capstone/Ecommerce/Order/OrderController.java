package com.Capstone.Ecommerce.Order;

import com.Capstone.Ecommerce.Utils.UtilsFunctions;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final UtilsFunctions utilsFunctions;
    private final OrderService orderService;

    @Value("${spring.kafka.producer.properties.spring.json.type.mapping}")
    private String typeMapping;


    @GetMapping("/")
    public ResponseEntity<List<OrderResponse>> getAllOrderOfUser(){
        return ResponseEntity.ok(orderService.getAllOrdersOfUser());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping("/")
    public ResponseEntity<Order> addOrder(@RequestBody @Valid OrderRequest request){
        System.out.println("Kafka Type Mapping: " + typeMapping);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addOrder(request));
    }
}
