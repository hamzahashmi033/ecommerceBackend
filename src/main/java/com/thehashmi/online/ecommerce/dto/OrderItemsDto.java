package com.thehashmi.online.ecommerce.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemsDto {
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
