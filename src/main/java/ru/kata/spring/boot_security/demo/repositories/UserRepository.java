package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserRepository {
    User findByUsername(String username);

    List<User> findAll();

    User save(User newUser);

    void deleteById(Integer id);

    User getById(Integer id);
}
