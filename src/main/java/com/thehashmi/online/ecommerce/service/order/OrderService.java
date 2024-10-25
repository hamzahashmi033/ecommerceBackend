package com.thehashmi.online.ecommerce.service.order;

import com.thehashmi.online.ecommerce.dto.OrderDto;
import com.thehashmi.online.ecommerce.model.*;
import com.thehashmi.online.ecommerce.model.enums.OrderStatus;
import com.thehashmi.online.ecommerce.repository.OrderRepository;
import com.thehashmi.online.ecommerce.repository.ProductRepository;
import com.thehashmi.online.ecommerce.service.cart.ICartService;
import com.thehashmi.online.ecommerce.utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItems> orderItemsList = createOrderItems(order,cart);
        order.setOrderItems(new HashSet<>(orderItemsList));
        order.setTotalAmount(getTotalPrice(orderItemsList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }
    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }
    private List<OrderItems> createOrderItems(Order order, Cart cart){
        List<OrderItems> orderItems = new ArrayList<>();
        for(CartItems item : cart.getItems()){
            Product product = item.getProduct();
            product.setInventory(product.getInventory() - item.getQuantity());
            productRepository.save(product);
            OrderItems orderItem =
                    new OrderItems(item.getQuantity(), item.getUnitPrice(),order,product);
            orderItems.add(orderItem);
        }
        return orderItems;

    }

    private BigDecimal getTotalPrice(List<OrderItems> orderItems){
        BigDecimal totalAmount = BigDecimal.ZERO;
        for(OrderItems item : orderItems){
            BigDecimal itemTotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }
        return totalAmount;
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        Order order =  orderRepository.findById(orderId)
                .orElseThrow(()-> new NotFoundException("Order not found!"));
        return convertToDto(order);
    }
    @Override
    public List<OrderDto> getUserOrders(Long userId){
        List<Order> orders =  orderRepository.findByUserId(userId);
        List<OrderDto> orderDtoList = new ArrayList<>();
        for(Order order : orders){
            orderDtoList.add(convertToDto(order));
        }
        return orderDtoList;
    }
    private OrderDto convertToDto(Order order){
        return modelMapper.map(order,OrderDto.class);
    }
}
