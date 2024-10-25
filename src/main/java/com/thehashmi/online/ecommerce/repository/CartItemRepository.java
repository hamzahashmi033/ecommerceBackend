package com.thehashmi.online.ecommerce.repository;

import com.thehashmi.online.ecommerce.model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItems,Long> {
    void deleteAllByCartId(Long id);
}
