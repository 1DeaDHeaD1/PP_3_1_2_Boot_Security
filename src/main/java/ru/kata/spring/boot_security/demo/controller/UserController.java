package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String printWelcome() {
        return "index";
    }

    @GetMapping(value = "/user")
    public String getUser(ModelMap model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(loggedInUser.getName());
        model.addAttribute("user", user);
        return "user";
    }

}