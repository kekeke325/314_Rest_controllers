package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
public class RestApiController {

    private final UserService userService;
    private final TemplateEngine templateEngine;
    private final RoleService roleService;

    @Autowired
    public RestApiController(UserService userService, TemplateEngine templateEngine, RoleService roleService) {
        this.userService = userService;
        this.templateEngine = templateEngine;
        this.roleService = roleService;
    }

    @GetMapping(value = "/auth/login", produces = MediaType.TEXT_HTML_VALUE)
    public String loginPage(HttpServletRequest request, HttpServletResponse response) {
        WebContext context = new WebContext(request, response, request.getServletContext());
        return templateEngine.process("login/login", context);
    }

    @GetMapping(value = "/user", produces = MediaType.TEXT_HTML_VALUE)
    public String userProfile(Authentication authentication,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        WebContext context = new WebContext(request, response, request.getServletContext());

        User user = userService.findByEmail(authentication.getName()).orElse(new User());
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .map(role -> role.split("_")[1])
                .toList();

        context.setVariable("user", user);
        context.setVariable("roles", roles);

        return templateEngine.process("user/user_index", context);
    }

    @GetMapping(value = "admin/users", produces = MediaType.TEXT_HTML_VALUE)
    public String adminProfile(Authentication authentication,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        WebContext context = new WebContext(request, response, request.getServletContext());

        User user = userService.findByEmail(authentication.getName()).orElse(new User());
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .map(role -> role.split("_")[1])
                .toList();

        context.setVariable("user", user);
        context.setVariable("roles", roles);
        context.setVariable("listRoles", roleService.findAll());

        return templateEngine.process("admin/index", context);
    }

    @GetMapping(value = "/api/users")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping("/api/users")
    public ResponseEntity<?> createUser(@RequestBody @Valid User user,
                                        BindingResult bindingResult) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "error.email", "This email is already in use");
        }

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        userService.add(user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/api/users")
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        userService.update(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.findById(id).ifPresent(u -> userService.deleteById(id));
        return ResponseEntity.ok().build();
    }
}