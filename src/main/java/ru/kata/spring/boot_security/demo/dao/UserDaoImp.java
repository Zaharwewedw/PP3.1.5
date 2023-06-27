package ru.kata.spring.boot_security.demo.dao;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class UserDaoImp implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserDaoImp() {
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT user FROM User user", User.class).getResultList();
    }

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
        Hibernate.initialize(user.getRoleSet());
    }

    @Override
    public void upDateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteUser(Long id) {
        entityManager.remove(getByIdUser(id));
    }

    @Override
    public User getByIdUser(Long id) {
        return entityManager.find(User.class, id);
    }

}