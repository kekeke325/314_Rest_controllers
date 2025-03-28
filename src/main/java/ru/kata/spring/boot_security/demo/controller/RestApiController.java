package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RestApiController {

    private final UserService userService;

    @Autowired
    public RestApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @Transactional
    public List<User> getAllUsers() {
        List<User> users = userService.findAll();
        users.forEach(user -> user.getRoles().size());
        return users;
    }

    @PostMapping("/users")
    public ResponseEntity<HttpStatus> createUser(@RequestBody @Valid User user,
                                                 BindingResult bindingResult) {
        Optional<User> userByEmail = userService.findByEmail(user.getEmail());
        if (userByEmail.isPresent()) {
            bindingResult.rejectValue("email", "error.email",
                    "This email is already in use");
        }
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new EntityNotFoundException();
        }

        userService.add(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/users")
    public ResponseEntity<HttpStatus> editUser(@RequestBody @Valid User user,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new EntityNotFoundException();
        }

        userService.update(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        if (userService.findById(id).isPresent()) {
            userService.deleteById(id);
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
