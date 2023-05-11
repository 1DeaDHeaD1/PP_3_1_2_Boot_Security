package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @PersistenceContext
    private EntityManager entityManager;

    private final UserDao userDao;
    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder encoder) {
        this.userDao = userDao;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void add(User user) {
        // TODO: 10.05.2023 Better to throw exception or refactor createUser()
        if (user.getUsername() == null || user.getUsername().isEmpty() || userDao.findByUsername(user.getUsername()) != null) {
            return;
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return;
        }
        user.setPassword(encoder.encode(user.getPassword()));
        for(Role role : user.getRoles()) {
            role.setUser(user);
        }
        userDao.add(user);
    }

    @Override
    public List<User> listUsers() {
        return userDao.listUsers();
    }

    @Override
    public User findUserById(Long id) {
        return userDao.findById(id);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userDao.deleteUser(userDao.findById(id));
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        for (Role role : user.getRoles()) {
            role.setUser(user);
        }
        userDao.update(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByUsername(username);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createUser(String username, String password, List<Role> authorities) {
        User user = userDao.findByUsername(username);
        if (user != null) {
            return;
        }
        user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setRoles(authorities);
        for (Role r : authorities) {
            r.setUser(user);
        }
        entityManager.persist(user);
    }

}
