package com.thehashmi.online.ecommerce.service.cart;

import com.thehashmi.online.ecommerce.dto.CartDto;
import com.thehashmi.online.ecommerce.model.Cart;
import com.thehashmi.online.ecommerce.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotaPrice(Long id);
    Long initializeNewCart();
    CartDto convertToCartDto(Cart cart);
    Cart getCartByUserId(Long userId);
}
