package com.thehashmi.online.ecommerce.service.user;

import com.thehashmi.online.ecommerce.dto.UserDto;
import com.thehashmi.online.ecommerce.model.User;
import com.thehashmi.online.ecommerce.request.CreateUserRequest;
import com.thehashmi.online.ecommerce.request.UpdateProductRequest;
import com.thehashmi.online.ecommerce.request.UpdateUserRequest;

import java.util.Optional;

public interface IUserService {
    User getUserById(Long id);
    User createUser(CreateUserRequest createUser);
    User updateuser(UpdateUserRequest updateUser, Long userId);
    void deleteUser(Long userId);
    UserDto convertToDto(User user);
}
