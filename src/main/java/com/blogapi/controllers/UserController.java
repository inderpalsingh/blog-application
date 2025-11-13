package com.blogapi.controllers;

import com.blogapi.dto.UserDto;
import com.blogapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto user = userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @PathVariable Integer userId, @RequestBody UserDto userDto){
        UserDto updatedUser = userService.updateUser(userId, userDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> singleUser(@PathVariable Integer userId){
        UserDto singleUser = userService.getUserById(userId);
        return new ResponseEntity<>(singleUser, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> userDtoList = userService.getAllUsers();
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId){
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();

    }

}
