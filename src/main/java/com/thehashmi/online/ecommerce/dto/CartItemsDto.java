package com.thehashmi.online.ecommerce.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemsDto {
    private Long itemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDto product;
}
