package com.Capstone.Ecommerce.Order;

import com.Capstone.Ecommerce.OrderLine.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {
}
