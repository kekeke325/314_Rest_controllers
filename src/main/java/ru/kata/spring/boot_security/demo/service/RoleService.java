package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Optional;
import java.util.Set;

public interface RoleService {

    Role save(Role role);

    Set<Role> findAll();

    Optional<Role> findById(Long id);

    void deleteById(Long id);

    Optional<Role> findByName(String name);

    Set<Role> findAllById(Set<Long> roleIds);
}
