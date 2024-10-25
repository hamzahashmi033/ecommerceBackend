package com.thehashmi.online.ecommerce.service.cart;

import com.thehashmi.online.ecommerce.model.CartItems;
import com.thehashmi.online.ecommerce.model.User;

public interface ICartItemService {
    void addItemToCart(Long cartId,Long productId,int quantity);
    void removeItemFromCart(Long cartId,Long productId);
    void updateItemQuantity(Long cartdId,Long productId,int quantity);
    CartItems getCartItem(Long cartId, Long productId);

}
