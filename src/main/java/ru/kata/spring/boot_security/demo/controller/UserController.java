package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUserInfo(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName()).orElse(new User());
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .map(role -> role.split("_")[1])
                .toList();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "/user/user_index";
    }

}
