package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    User save(User user);

    List<User> findAll();

    Optional<User> findById(Long id);

    void updateUser(User user, Set<Role> roles);

    void deleteById(Long id);

    Optional<User> findByEmail(String email);

}