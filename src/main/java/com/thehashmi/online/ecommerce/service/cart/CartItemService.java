package com.thehashmi.online.ecommerce.service.cart;

import com.thehashmi.online.ecommerce.model.Cart;
import com.thehashmi.online.ecommerce.model.CartItems;
import com.thehashmi.online.ecommerce.model.Product;
import com.thehashmi.online.ecommerce.model.User;
import com.thehashmi.online.ecommerce.repository.CartItemRepository;
import com.thehashmi.online.ecommerce.repository.CartRepository;
import com.thehashmi.online.ecommerce.service.product.IProductService;
import com.thehashmi.online.ecommerce.utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final ICartService cartService;
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        // 1: Get the cart by ID (assuming cartService throws an exception if cart not found)
        Cart cart = cartService.getCart(cartId);

        // 2: Get the product by ID (assuming productService throws an exception if product not found)
        Product product = productService.getProductById(productId);

        // 3: Check if the product already exists in the cart
        CartItems cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        // 4: If the product is already in the cart, update its quantity
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            // 5: If the product is not in the cart, add it as a new item
            cartItem = new CartItems();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());

        }
        cartItem.setToPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);

    }


    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItems itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Product not found"));
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartdId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartdId);
        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent( item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setToPrice();
                });
        BigDecimal totalAmount = cart.getItems()
                .stream().map(CartItems ::getTotalPrice).reduce(BigDecimal.ZERO,BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }
    @Override
    public CartItems getCartItem(Long cartId,Long productId){
        Cart cart = cartService.getCart(cartId);
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item not found"));

    }


}
