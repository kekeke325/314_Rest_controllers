package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public String getUsersList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/admin/index";
    }

    @GetMapping("/users/new")
    public String createUserForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("listRoles", roleService.findAll());

        return "/admin/add";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult, Model model) {
        Optional<User> userByEmail = userService.findByEmail(user.getEmail());
        if (userByEmail.isPresent()) {
            bindingResult.rejectValue("email", "error.email",
                    "This email is already in use");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("listRoles", roleService.findAll());
            return "/admin/add";
        }

        this.userService.save(user);
        return "redirect:/admin/users/";
    }

    @GetMapping("/users/edit")
    public String editUserForm(@RequestParam("id") Long id, Model model) {
        Optional<User> userById = userService.findById(id);

        if (userById.isPresent()) {
            model.addAttribute("user", userById.get());
            model.addAttribute("listRoles", roleService.findAll());
            return "/admin/edit";
        } else {
            return "redirect:/admin/users";
        }
    }

    @PatchMapping("/users/edit")
    public String editUser(@ModelAttribute("user") @Valid User updatedUser,
                           BindingResult bindingResult, Model model) {
        Optional<User> userByEmail = userService.findByEmail(updatedUser.getEmail());
        if (userByEmail.isPresent() && (!userByEmail.get().getId().equals(updatedUser.getId()))) {
            bindingResult.rejectValue("email", "error.email", "This email is already in use");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("listRoles", roleService.findAll());
            return "/admin/edit";
        }

        userService.updateUser(updatedUser);
        return "redirect:/admin/users";
    }

    @DeleteMapping("/users/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        if (userService.findById(id).isPresent()) {
            userService.deleteById(id);
        }
        return "redirect:/admin/users";
    }
}