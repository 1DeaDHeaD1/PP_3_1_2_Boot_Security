package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    void add(User user);

    List<User> listUsers();

    User findUserById(Long id);

    void deleteUserById(Long id);

    void updateUser(User user);

    void createUser(String username, String password, List<Role> authorities);

}
