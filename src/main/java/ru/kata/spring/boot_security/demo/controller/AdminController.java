package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.constant.RoleConst;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping(value = "/admin", path = "/admin")
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

    @GetMapping(value = {"", "/users"})
    public String printWelcome(ModelMap model, @ModelAttribute(name = "newUser") User newUser, @ModelAttribute(name = "editUser") User editUser) {
        model.addAttribute("users", userService.listUsers());
        return "users";
    }

//    @PostMapping(value = {"", "/users"})
//    public String createUser(ModelMap model, @ModelAttribute("user") User user) {
//        userService.add(user);
//        return "redirect:/admin/users";
//    }
//
//    @PostMapping(value = {"/delete/{id}","/users/delete/{id}"})
//    public String deleteUser(ModelMap model, @PathVariable("id") long id) {
//        userService.deleteUserById(id);
//        model.addAttribute("users", userService.listUsers());
//        return "redirect:/admin/users";
//    }
//
//    @PostMapping(value = {"/edit/{id}","/users/edit/{id}"})
//    public String editUser(ModelMap model, @ModelAttribute("editUser") User user) {
//        userService.updateUser(user);
//        model.addAttribute("users", userService.listUsers());
//        return "redirect:/admin/users";
//    }

    @PostConstruct
    private void init() {
        userService.createUser("user", "user", Collections.singletonList(new Role("USER")));
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("ADMIN"));
        roles.add(new Role("USER"));
        userService.createUser("admin", "admin", roles);
    }

}
