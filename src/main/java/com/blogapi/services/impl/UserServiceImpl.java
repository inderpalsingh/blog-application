package com.blogapi.services.impl;

import com.blogapi.dto.UserDto;
import com.blogapi.entities.User;
import com.blogapi.exceptions.ResourceNotFoundException;
import com.blogapi.repositories.UserRepository;
import com.blogapi.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User mappedUser = modelMapper.map(userDto, User.class);
        mappedUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = userRepository.save(mappedUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto updateUser(Integer userId, UserDto userDto) {
        User userFound = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
        userFound.setName(userDto.getName());
        userFound.setAge(userDto.getAge());
        userFound.setGender(userDto.getGender());
        userFound.setEmail(userDto.getEmail());
        userFound.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User updatedUser = userRepository.save(userFound);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User userFound = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
        return modelMapper.map(userFound, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
    }

    @Override
    public void deleteUser(Integer userId) {
        User userFound = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
        userRepository.delete(userFound);

    }
}
