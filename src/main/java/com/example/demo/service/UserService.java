package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleSaveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> handleFindAllUsers() {
        return userRepository.findAll();
    }

    public User handleFindUserById(Long id) {
        return userRepository.findOneById(id);
    }

    public void handleDeleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
