package com.thehashmi.online.ecommerce.service.order;

import com.thehashmi.online.ecommerce.dto.OrderDto;
import com.thehashmi.online.ecommerce.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
}
