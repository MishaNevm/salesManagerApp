package com.example.userService.services;

import com.example.userService.models.User;
import com.example.userService.repositoryes.UserRepository;
import com.example.userService.util.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(int id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public void save(User user) {
        user.setCreatedAt(new Date());
        userRepository.save(user);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public void delete (int id) {
        userRepository.deleteById(id);
    }
}
