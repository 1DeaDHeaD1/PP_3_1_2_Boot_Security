package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {

    User add(User user);

    List<User> listUsers();

    User findById(Long id);

    User findByUsername(String username);

    User update(User user);

    User deleteUser(User user);

}
