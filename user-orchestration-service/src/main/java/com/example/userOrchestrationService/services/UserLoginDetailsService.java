package com.example.userOrchestrationService.services;

import com.example.userOrchestrationService.dto.UserLogin;
import com.example.userOrchestrationService.models.User;
import com.example.userOrchestrationService.repositoryes.UserRepository;
import com.example.userOrchestrationService.security.UserLoginDetails;
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
