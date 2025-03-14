package com.sharif.eshop.controller;

import com.sharif.eshop.model.Cart;
import com.sharif.eshop.model.CartItem;
import com.sharif.eshop.model.User;
import com.sharif.eshop.response.ApiResponse;
import com.sharif.eshop.service.cart.ICartItemService;
import com.sharif.eshop.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;

    @GetMapping("/user/{userId}/cart")
    public ResponseEntity<ApiResponse> getUserCart(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(new ApiResponse("User cart retrieved successfully", cart));
    }

    @DeleteMapping("/cart/{cartId}/clear")
    public void clearCart( @PathVariable Long cartId) {
        cartService.clearCart(cartId);
    }


}
