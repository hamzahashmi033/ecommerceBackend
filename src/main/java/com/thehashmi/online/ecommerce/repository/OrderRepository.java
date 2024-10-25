package com.thehashmi.online.ecommerce.repository;

import com.thehashmi.online.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order>  findByUserId(Long Id);
}
