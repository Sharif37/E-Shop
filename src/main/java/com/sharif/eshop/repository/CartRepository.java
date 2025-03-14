package com.sharif.eshop.repository;

import com.sharif.eshop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser_UserId(Long userId);
}
