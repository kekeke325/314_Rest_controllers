package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Optional;
import java.util.Set;

public interface RoleService {

    Set<Role> findAll();

    Role save(Role role);

    Optional<Role> findById(Long id);

    Optional<Role> findByName(String name);

    Set<Role> findAllById(Set<Long> roleIds);

    void deleteById(Long id);
}
