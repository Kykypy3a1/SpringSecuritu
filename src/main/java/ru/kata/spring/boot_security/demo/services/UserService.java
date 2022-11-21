package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();

    User saveUser(User user, String role);

    void removeUserById(Integer id);

    User getUserById(Integer id);

    User findByUsername(String username);

    void edit(User user, Integer id, String role);
}
