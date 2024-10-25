package com.thehashmi.online.ecommerce.service.cart;

import com.thehashmi.online.ecommerce.dto.CartDto;
import com.thehashmi.online.ecommerce.dto.CartItemsDto;
import com.thehashmi.online.ecommerce.dto.ProductDto;
import com.thehashmi.online.ecommerce.model.Cart;
import com.thehashmi.online.ecommerce.model.CartItems;
import com.thehashmi.online.ecommerce.model.User;
import com.thehashmi.online.ecommerce.repository.CartItemRepository;
import com.thehashmi.online.ecommerce.repository.CartRepository;
import com.thehashmi.online.ecommerce.utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);
    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotaPrice(Long id) {
        Cart cart = getCart(id);
       return cart.getTotalAmount();
    }

    @Override
    public Long initializeNewCart() {
        Cart newCart = new Cart();
        Long newCartId = cartIdGenerator.incrementAndGet();
        newCart.setId(newCartId);
        return cartRepository.save(newCart).getId();
    }

    @Override
    public CartDto convertToCartDto(Cart cart) {
        // Create a new CartDto object and map basic properties
        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        cartDto.setCartId(cart.getId());
        cartDto.setTotalAmount(cart.getTotalAmount());

        // Create a set to hold CartItemsDto
        Set<CartItemsDto> cartItemsDtoSet = new HashSet<>();

        // Iterate over the Cart items and convert each CartItems to CartItemsDto
        for (CartItems item : cart.getItems()) {
            CartItemsDto cartItemsDto = new CartItemsDto();
            cartItemsDto.setItemId(item.getId());
            cartItemsDto.setQuantity(item.getQuantity());
            cartItemsDto.setUnitPrice(item.getUnitPrice());

            // Map the product to ProductDto if needed
            ProductDto productDto = modelMapper.map(item.getProduct(), ProductDto.class);
            cartItemsDto.setProduct(productDto);

            cartItemsDtoSet.add(cartItemsDto);
        }

        // Set the items in the CartDto
        cartDto.setItems(cartItemsDtoSet);

        return cartDto;
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

}
