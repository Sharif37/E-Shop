package com.sharif.eshop.controller;

import com.sharif.eshop.dto.OrderDto;
import com.sharif.eshop.model.Order;
import com.sharif.eshop.response.ApiResponse;
import com.sharif.eshop.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;


    @PostMapping("/user/order")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam Long userId) {
        Order order = orderService.placeOrder(userId);
        OrderDto orderDto =  orderService.convertToDto(order);
        return ResponseEntity.ok(new ApiResponse("Order placed successfully", orderDto));
    }


    @GetMapping("/user/{userId}/orders")
    public ResponseEntity<ApiResponse>getUserOrders(@PathVariable Long userId){
        List<Order> orders = orderService.getOrdersByUserId(userId);
        List<OrderDto> orderDtos = orderService.getUserOrders(orders);
        return ResponseEntity.ok(new ApiResponse("User orders retrieved successfully", orderDtos));
    }


}
