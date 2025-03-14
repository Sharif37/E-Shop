package com.sharif.eshop.repository;

import com.sharif.eshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);
    CartItem findByCartIdAndProductId(Long cartId, Long productId);
    void deleteByCartId(Long cartId);
    void deleteByCartIdAndProductId(Long cartId, Long productId);

    List<CartItem> findByProductId(Long productId);

    void deleteAllByCartId(Long cartId);
}
