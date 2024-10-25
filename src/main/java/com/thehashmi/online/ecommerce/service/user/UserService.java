package com.thehashmi.online.ecommerce.service.user;

import com.thehashmi.online.ecommerce.dto.UserDto;
import com.thehashmi.online.ecommerce.model.User;
import com.thehashmi.online.ecommerce.repository.UserRepository;
import com.thehashmi.online.ecommerce.request.CreateUserRequest;
import com.thehashmi.online.ecommerce.request.UpdateUserRequest;
import com.thehashmi.online.ecommerce.utils.AlreadyExistsException;
import com.thehashmi.online.ecommerce.utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new NotFoundException("User not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        // Check if the email already exists in the database
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Oops! " + request.getEmail() + " already exists!");
        }

        // If email doesn't exist, create a new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        // Save the new user to the repository
        return userRepository.save(user);
    }

    @Override
    public User updateuser(UpdateUserRequest updateUser, Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setFirstName(updateUser.getFirstName());
            existingUser.setLastName(updateUser.getLastName());
            return userRepository.save(existingUser);
        } else {
            throw new NotFoundException("User not found!");
        }
    }





    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository ::delete,()->{
            throw new NotFoundException("User not found!");
        });
    }
    @Override
    public UserDto convertToDto(User user){
        return modelMapper.map(user,UserDto.class);
    }
}
