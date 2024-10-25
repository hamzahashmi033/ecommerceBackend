package com.thehashmi.online.ecommerce.controller;


import com.thehashmi.online.ecommerce.dto.OrderDto;
import com.thehashmi.online.ecommerce.model.Order;
import com.thehashmi.online.ecommerce.response.ApiResponse;
import com.thehashmi.online.ecommerce.service.order.IOrderService;
import com.thehashmi.online.ecommerce.utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("{api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId){
        try{
            Order order = orderService.placeOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Item Order success!",order));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops!",e.getMessage()));
        }
    }
    @GetMapping("{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
        try{
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Item Order success!",order));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops!",e.getMessage()));
        }
    }
    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse> getUsersOrder(@PathVariable Long userId){
        try{
            List<OrderDto> userOrder = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Success",userOrder));
        }catch(NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops!",e.getMessage()));
        }
    }


}
