package com.thehashmi.online.ecommerce.controller;

import com.thehashmi.online.ecommerce.dto.UserDto;
import com.thehashmi.online.ecommerce.model.User;
import com.thehashmi.online.ecommerce.request.CreateUserRequest;
import com.thehashmi.online.ecommerce.request.UpdateUserRequest;
import com.thehashmi.online.ecommerce.response.ApiResponse;
import com.thehashmi.online.ecommerce.service.user.IUserService;
import com.thehashmi.online.ecommerce.utils.AlreadyExistsException;
import com.thehashmi.online.ecommerce.utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("{api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        try{
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("Success",userDto));

        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request){
        try{
            User user = userService.createUser(request);
            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("Success",userDto));
        }catch (AlreadyExistsException e){
            return ResponseEntity.status((HttpStatus.INTERNAL_SERVER_ERROR)).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request,@PathVariable Long userId){
        try{
            User user = userService.updateuser(request,userId);
            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("success",userDto));
        }catch (NotFoundException e){
            return ResponseEntity.status((HttpStatus.INTERNAL_SERVER_ERROR)).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        try{
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Delete User Successfully",null));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
