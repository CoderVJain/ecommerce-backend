package com.example.E_CommerceSpring.repo;

import com.example.E_CommerceSpring.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
