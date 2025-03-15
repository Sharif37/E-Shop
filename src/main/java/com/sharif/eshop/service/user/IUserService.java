package com.sharif.eshop.service.user;

import com.sharif.eshop.dto.UserDto;
import com.sharif.eshop.model.User;
import com.sharif.eshop.request.CreateUserRequest;
import com.sharif.eshop.request.UpdateUserRequest;

public interface IUserService {

    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    User getUserById(Long userId);
    void deleteUserById(Long userId);


    UserDto convertToDto(User user);

    User getAuthenticatedUser();
}
