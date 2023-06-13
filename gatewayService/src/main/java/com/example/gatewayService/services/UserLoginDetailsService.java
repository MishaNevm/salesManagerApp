package com.example.gatewayService.services;

import com.example.gatewayService.dto.UserLogin;
import com.example.gatewayService.models.User;
import com.example.gatewayService.repositoryes.UserRepository;
import com.example.gatewayService.security.UserLoginDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserLoginDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserLoginDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserLoginDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userFromDb = userRepository.findByEmail(username);
        if (userFromDb.isEmpty()) throw new UsernameNotFoundException("Пользователь не найден");
        UserLogin userLogin = new UserLogin();
        User user = userFromDb.get();
        userLogin.setEmail(user.getEmail());
        userLogin.setPassword(user.getPassword());
        return new UserLoginDetails(userLogin);
    }
}
