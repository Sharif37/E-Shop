package com.sharif.eshop.service.user;

import com.sharif.eshop.dto.*;
import com.sharif.eshop.model.*;
import com.sharif.eshop.repository.OrderRepository;
import com.sharif.eshop.repository.UserRepository;
import com.sharif.eshop.request.CreateUserRequest;
import com.sharif.eshop.request.UpdateUserRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(newUser -> {
                    User user = new User();
                    user.setFirstName(newUser.getFirstName());
                    user.setLastName(newUser.getLastName());
                    user.setEmail(newUser.getEmail());
                    user.setPassword(passwordEncoder.encode(newUser.getPassword()));
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new EntityExistsException("User with email : " + request.getEmail() + " already exists!"));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(
                () -> new EntityNotFoundException("User with Id : " + userId + " not found!")
        );

    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with Id : " + userId + " not found!")
        );
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete,
                () -> {
                    throw new EntityNotFoundException("User with Id : " + userId + " not found!");
                }
        );

    }

//    @Override
//    public UserDto convertToDto(User user) {
//        return modelMapper.map(user, UserDto.class);
//    }

    @Override
    public UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getUserId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());

        // Map orders
        if (user.getOrder() != null) {
            List<OrderDto> orderDtos = user.getOrder().stream()
                    .map(this::convertOrderToDto)
                    .toList();
            userDto.setOrders(orderDtos);
        }

        // Map cart
        if (user.getCart() != null) {
            userDto.setCart(convertCartToDto(user.getCart()));
        }

        return userDto;
    }

    private OrderDto convertOrderToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getOrderId());
        orderDto.setUserId(order.getUser().getUserId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setStatus(order.getOrderStatus().name());

        if (order.getOrderItems() != null) {
            List<OrderItemDto> orderItems = order.getOrderItems().stream()
                    .map(this::convertOrderItemToDto)
                    .toList();
            orderDto.setItems(orderItems);
        }

        return orderDto;
    }

    private CartDto convertCartToDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setCartId(cart.getId());
        cartDto.setTotalAmount(cart.getTotalAmount());

        if (cart.getCartItems() != null) {
            Set<CartItemDto> cartItems = cart.getCartItems().stream()
                    .map(this::convertCartItemToDto)
                    .collect(Collectors.toSet());
            cartDto.setItems(cartItems);
        }

        return cartDto;
    }

    private OrderItemDto convertOrderItemToDto(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(orderItem.getProduct().getId());
        orderItemDto.setProductName(orderItem.getProduct().getName());
        orderItemDto.setProductBrand(orderItem.getProduct().getBrand());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setPrice(orderItem.getPrice());
        return orderItemDto;
    }

    private CartItemDto convertCartItemToDto(CartItem cartItem) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setItemId(cartItem.getCartItemId());
        cartItemDto.setQuantity(cartItem.getQuantity());
        cartItemDto.setUnitPrice(cartItem.getUnitPrice());
        cartItemDto.setTotalPrice(cartItem.getTotalPrice());

        // Map product details
        if (cartItem.getProduct() != null) {
            cartItemDto.setProduct(convertProductToDto(cartItem.getProduct()));
        }

        return cartItemDto;
    }

    private ProductDto convertProductToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setBrand(product.getBrand());
        productDto.setPrice(product.getPrice());
        productDto.setInventory(product.getInventory());
        productDto.setDescription(product.getDescription());
        productDto.setCategory(product.getCategory());

        if (product.getImages() != null) {
            List<ImageDto> imageDtos = product.getImages().stream()
                    .map(this::convertImageToDto)
                    .toList();
            productDto.setImages(imageDtos);
        }

        return productDto;
    }

    private ImageDto convertImageToDto(Image image) {
        ImageDto imageDto = new ImageDto();
        imageDto.setId(image.getId());
        imageDto.setFilename(image.getFilename());
        imageDto.setDownloadUrl(image.getDownloadUrl());
        return imageDto;
    }


    @Override
    public User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new EntityNotFoundException("Log in Required!"));
    }


}
