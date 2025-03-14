package com.sharif.eshop.service.order;

import com.sharif.eshop.dto.OrderDto;
import com.sharif.eshop.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId) ;
    List<Order> getOrdersByUserId(Long userId);
    Order getOrderById(Long orderId);

    OrderDto convertToDto(Order order);

    List<OrderDto> getUserOrders(List<Order> orders);
}
