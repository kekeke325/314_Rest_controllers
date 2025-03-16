package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService,
                           RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    @Transactional
    public String getUsersList(Model model, Principal principal) {
        List<User> users = userService.findAll();
        users.forEach(user -> user.getRoles().size());
        User user = userService.findByEmail(principal.getName()).orElse(new User());
        model.addAttribute("user", user);
        model.addAttribute("users", users);
        return "/admin/index";
    }

    @GetMapping("/users/new")
    public String createUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "/admin/add";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("listRoles", roleService.findAll());
            return "redirect:/admin/users/";
        }

        this.userService.save(user);
        return "redirect:/admin/users/";
    }

    @PatchMapping("/users/edit")
    public String editUser(@ModelAttribute("user") @Valid User updatedUser,
                           @RequestParam("roles") Set<Long> roleIds,
                           BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("listRoles", roleService.findAll());
            return "redirect:/admin/users";
        }

        Set<Role> roles = roleService.findAllById(roleIds);

        userService.updateUser(updatedUser, roles);

        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        if (userService.findById(id).isPresent()) {
            userService.deleteById(id);
        }
        return "redirect:/admin/users";
    }
}