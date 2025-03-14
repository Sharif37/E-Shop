package com.sharif.eshop.service.cart;

import com.sharif.eshop.model.Cart;
import com.sharif.eshop.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long cartId);
    Cart getCartByUserId(Long userId);
    void clearCart(Long cartId);

    Cart initNewCartForUser(User user);

    BigDecimal getCartTotalPrice(Long cartId);


}
