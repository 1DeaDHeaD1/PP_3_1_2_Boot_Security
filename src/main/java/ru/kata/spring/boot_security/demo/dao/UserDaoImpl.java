package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User add(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public List<User> listUsers() {
        TypedQuery<User> query = entityManager.createQuery("from User", User.class);
        return query.getResultList();
    }

    @Override
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User findByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery("from User u where u.username = :username", User.class);
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst().orElse(null);
//        return query.getSingleResult();
    }

    @Override
    public User update(User user) {
        return entityManager.merge(user);
    }

    @Override
    public User deleteUser(User user) {
        entityManager.remove(user);
        return user;
    }

}
