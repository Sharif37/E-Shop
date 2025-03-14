package com.sharif.eshop.service.cart;

import com.sharif.eshop.model.Cart;
import com.sharif.eshop.model.User;
import com.sharif.eshop.repository.CartItemRepository;
import com.sharif.eshop.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    @Override
    public Cart getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new EntityNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);

        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUser_UserId(userId);

    }

    @Override
    public void clearCart(Long cartId) {
        Cart cart = getCart(cartId);
        cartItemRepository.deleteAllByCartId(cartId);
        cart.clearCart();
        cartRepository.deleteById(cartId);

    }



    @Override
    public Cart initNewCartForUser(User user) {

        return Optional.ofNullable(getCartByUserId(user.getUserId()))
                .orElseGet(
                        ()->{
                            Cart cart = new Cart();
                            cart.setUser(user);
                            return cartRepository.save(cart);
                        }
                );
    }

    @Override
    public BigDecimal getCartTotalPrice(Long cartId) {
        Cart cart = getCart(cartId);
        return cart.getTotalAmount();
    }
}
