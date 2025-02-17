package com.Capstone.Ecommerce.Order;

import com.Capstone.Ecommerce.Exceptions.InsufficientQuantityException;
import com.Capstone.Ecommerce.Exceptions.ResourceNotFoundException;
import com.Capstone.Ecommerce.Kafka.OrderProducer;
import com.Capstone.Ecommerce.OrderLine.OrderLineRequest;
import com.Capstone.Ecommerce.Product.ProductFeignClient;
import com.Capstone.Ecommerce.Product.ProductRequest;
import com.Capstone.Ecommerce.Product.ProductResponse;
import com.Capstone.Ecommerce.User.UserFeignClient;
import com.Capstone.Ecommerce.User.UserResponse;
import com.Capstone.Ecommerce.Utils.UtilsFunctions;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final OrderMapper orderMapper;
    private final UserFeignClient userFeignClient;
    private final UtilsFunctions utilsFunctions;
    private final ProductFeignClient productFeignClient;
    private final OrderProducer orderProducer;

    @Value("${security.apiKey.apiSecret}")
    private String apiKey;

    @Value("${feignClient.config.product-url}")
    private String productUrl;

    public List<OrderResponse> getAllOrdersOfUser() {
        Long userId = utilsFunctions.extractIdFromPrincipal();
        return orderRepo.findAll().stream()
                .filter(order -> Objects.equals(order.getUserId(), userId))
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    public Order getOrderById(Long id){
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with the given id do not exist"));

        Long userId = order.getUserId();

        if(!Objects.equals(userId, utilsFunctions.extractIdFromPrincipal())){
            throw new AuthorizationDeniedException("you are not authorised to view this resource");
        }

        return order;
    }

    @Transactional
    public Order addOrder(OrderRequest request) {
        Long userId = utilsFunctions.extractIdFromPrincipal();
        UserResponse userResponse = userFeignClient.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("current user id do not exist int DB"));

        System.out.println("user service via feign client: " + userResponse.id());
        List<ProductResponse> purchasedProductsList = new ArrayList<>();

        for(int i=0;i<request.orderLine().size();++i){
            var orderedProduct = request.orderLine().get(i);
            ProductResponse productResponse ;
            try{
                System.out.println(productUrl);
                productResponse = productFeignClient.getProductById(orderedProduct.id())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                String.format("ordered product with id %d is not available", orderedProduct.id())
                        ));
            } catch (Exception e) {
                throw new ResourceNotFoundException(
                        String.format("ordered product with id %d is not available", orderedProduct.id())
                );
            }


            if(productResponse.availableQuantity() < orderedProduct.quantity()){
                throw new InsufficientQuantityException(
                        String.format("the ordered quantity of product with id %d is greater than available quantity %f",orderedProduct.id(),productResponse.availableQuantity())
                );
            }

            purchasedProductsList.add(productResponse);
        }

        BigDecimal amount = new BigDecimal(0);

        for(int i=0;i<request.orderLine().size();++i){
            ProductResponse currProduct = purchasedProductsList.get(i);
            OrderLineRequest orderedProduct = request.orderLine().get(i);

            double newQuantity = currProduct.availableQuantity() - orderedProduct.quantity();

            amount = amount.add(BigDecimal.valueOf(orderedProduct.quantity()).multiply(new BigDecimal(currProduct.price())));

            /// product stock update via feign client
//            ProductResponse updatedProduct = productFeignClient.updateProductById(
//                            orderedProduct.id()
//                            ,new ProductRequest(orderedProduct.id(),newQuantity),
//                            apiKey)
//                    .orElseThrow(() -> new RuntimeException("something went wrong while placing the order. please try again"));


            /// product stock update via kafka
            orderProducer.sendOrderLineConfirmation(new ProductRequest(orderedProduct.id(),newQuantity));
        }

        Order order = orderMapper.toOrder(request);
        order.setUserId(utilsFunctions.extractIdFromPrincipal());
        order.setTotalAmount(amount);

        return orderRepo.save(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepo.findAll().stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }
}
