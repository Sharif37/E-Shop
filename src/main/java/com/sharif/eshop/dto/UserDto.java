package com.sharif.eshop.dto;

import com.sharif.eshop.model.Cart;
import com.sharif.eshop.model.Role;
import lombok.Data;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Data
public class UserDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Cart cart;
    private List<OrderDto> order;
    private Collection<Role> roles = new HashSet<>();
}
