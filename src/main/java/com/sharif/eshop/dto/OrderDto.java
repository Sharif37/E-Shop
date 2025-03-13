package com.sharif.eshop.dto;

import com.sharif.eshop.enums.OrderStatus;
import com.sharif.eshop.model.OrderItem;
import com.sharif.eshop.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class OrderDto {
    private Long orderId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
//    private User user ;
//    private Set<OrderItem> orderItems = new HashSet<>();
}
