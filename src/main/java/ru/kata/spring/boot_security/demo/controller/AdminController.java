package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.constant.RoleConst;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(final UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute(name = "allRoles")
    public RoleConst[] getAllRoles() {
        return RoleConst.values();
    }

    @GetMapping(value = {"/admin", "/admin/users"})
    public String printWelcome(ModelMap model, @ModelAttribute(name = "newUser") User newUser) {
        model.addAttribute("users", userService.listUsers());
        return "users";
    }

    @PostMapping(value = "admin/users")
    public String createUser(ModelMap model, @ModelAttribute("user") User user) {
        userService.add(user);
        return "redirect:/admin/users";
    }

    @PostMapping(value = "admin/users/delete/{id}")
    public String deleteUser(ModelMap model, @PathVariable("id") long id) {
        userService.deleteUserById(id);
        model.addAttribute("users", userService.listUsers());
        return "redirect:/admin/users";
    }

    @GetMapping(value = "admin/users/edit/{id}")
    public String getUser(ModelMap model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.findUserById(id));
        return "user-edit";
    }

    @PostMapping(value = "admin/users/edit/{id}")
    public String editUser(ModelMap model, @ModelAttribute("user") User user) {
        userService.updateUser(user);
        model.addAttribute("users", userService.listUsers());
        return "redirect:/admin/users";
    }

    @PostConstruct
    private void init() {
        userService.createUser("user", "user", Collections.singletonList(new Role("USER")));
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("ADMIN"));
        roles.add(new Role("USER"));
        userService.createUser("admin", "admin", roles);
    }

}
