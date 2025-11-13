package com.blogapi.config.security;


import com.blogapi.entities.User;
import com.blogapi.exceptions.ResourceNotFoundException;
import com.blogapi.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // loading use from database by username
        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User", "email : " + username, 0));

        CustomUserDetail customUserDetail = new CustomUserDetail(user);

        return customUserDetail;
    }
}
