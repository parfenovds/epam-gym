package com.epam.repository;

import com.epam.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends BaseRepositoryImpl<User>{
  private final SessionFactory sessionFactory;
  public UserRepository(SessionFactory sessionFactory) {
    super(sessionFactory, User.class);
    this.sessionFactory = sessionFactory;
  }

  public Boolean usernameToPasswordMatchingCheck(String username, String password) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class)
        .setParameter("username", username)
        .setParameter("password", password)
        .uniqueResultOptional()
        .isPresent();
  }
}
