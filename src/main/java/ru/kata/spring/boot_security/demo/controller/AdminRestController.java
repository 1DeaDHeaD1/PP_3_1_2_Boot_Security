package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.constant.RoleConst;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestController {

    private final UserService userService;

    @Autowired
    public AdminRestController(final UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute(name = "allRoles")
    public RoleConst[] getAllRoles() {
        return RoleConst.values();
    }

    @GetMapping(value = {"", "/users"})
    public List<User> findUsers() {
        return userService.listUsers();
    }

    @PostMapping(value = {"", "/users"})
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (user.getUsername() == null || "".equals(user.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        userService.add(user);
        return ResponseEntity.ok("Success putted! " + user.getId());
    }
    @GetMapping(value = {"/edit/{id}", "/users/edit/{id}"})
    public User getUser(@PathVariable("id") long id) {
        return userService.findUserById(id);
    }

    @PutMapping(value = {"/edit/{id}", "/users/edit/{id}"})
    public ResponseEntity<?> editUser(@PathVariable("id") Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateUser(user);
        return ResponseEntity.ok("Success putted! " + user.getId());
    }

    @DeleteMapping(value = {"/{id}", "/users/{id}"})
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("Success patched!" + id);
    }

}
