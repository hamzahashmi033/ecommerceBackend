package com.thehashmi.online.ecommerce.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartDto {
    private  Long cartId;
    private Set<CartItemsDto> items;
    private BigDecimal totalAmount;
}
