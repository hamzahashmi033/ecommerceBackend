package com.thehashmi.online.ecommerce.repository;

import com.thehashmi.online.ecommerce.model.Cart;
import com.thehashmi.online.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findByUserId(Long id);
}
