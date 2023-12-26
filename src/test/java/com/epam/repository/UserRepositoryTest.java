package com.epam.repository;

import com.epam.entity.User;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UserRepositoryTest {

  @Mock
  private SessionFactory sessionFactory;

  @Mock
  private Session session;

  @Mock
  private Query<User> query;

  @InjectMocks
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testUsernameToPasswordMatchingCheck_MatchingCredentials_ReturnsTrue() {
    String username = "testUsername";
    String password = "testPassword";

    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class))
        .thenReturn(query);
    when(query.setParameter("username", username)).thenReturn(query);
    when(query.setParameter("password", password)).thenReturn(query);
    when(query.uniqueResultOptional()).thenReturn(Optional.of(new User()));

    boolean result = userRepository.usernameToPasswordMatchingCheck(username, password);

    assertTrue(result);

    verify(sessionFactory).getCurrentSession();
    verify(session).createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
    verify(query).setParameter("username", username);
    verify(query).setParameter("password", password);
    verify(query).uniqueResultOptional();
  }

  @Test
  void testUsernameToPasswordMatchingCheck_NonMatchingCredentials_ReturnsFalse() {
    String username = "testUsername";
    String password = "testPassword";

    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class))
        .thenReturn(query);
    when(query.setParameter("username", username)).thenReturn(query);
    when(query.setParameter("password", password)).thenReturn(query);
    when(query.uniqueResultOptional()).thenReturn(Optional.empty());

    boolean result = userRepository.usernameToPasswordMatchingCheck(username, password);

    assertFalse(result);

    verify(sessionFactory).getCurrentSession();
    verify(session).createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
    verify(query).setParameter("username", username);
    verify(query).setParameter("password", password);
    verify(query).uniqueResultOptional();
  }

  @Test
  void testGetUserById() {
    Long id = 1L;
    User expectedUser = new User();

    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.get(User.class, id)).thenReturn(expectedUser);

    Optional<User> result = userRepository.get(id);

    assertTrue(result.isPresent());
    assertEquals(expectedUser, result.get());

    verify(sessionFactory).getCurrentSession();
    verify(session).get(User.class, id);
  }

  @Test
  void testDeleteUserById() {
    Long id = 1L;
    User user = new User();

    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.get(User.class, id)).thenReturn(user);

    userRepository.delete(id);

    verify(sessionFactory, times(2)).getCurrentSession(); // Ожидаем два вызова getCurrentSession()
    verify(session).get(User.class, id);
    verify(session).remove(user);
  }

  @Test
  void testUpdateUser() {
    User user = new User();

    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.merge(user)).thenReturn(user);

    User updatedUser = userRepository.update(user);

    assertEquals(user, updatedUser);

    verify(sessionFactory).getCurrentSession();
    verify(session).merge(user);
  }

  @Test
  void testCreateUser() {
    User user = new User();

    when(sessionFactory.getCurrentSession()).thenReturn(session);

    User createdUser = userRepository.create(user);

    assertEquals(user, createdUser);

    verify(sessionFactory).getCurrentSession();
    verify(session).persist(user);
  }

  @Test
  void testGetAllUsers() {
    List<User> expectedUsers = Collections.singletonList(new User());

    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.createQuery("SELECT entity FROM User entity ORDER BY entity.id", User.class)).thenReturn(query);
    when(query.getResultList()).thenReturn(expectedUsers);

    List<User> result = userRepository.getAll();

    assertEquals(expectedUsers.size(), result.size());
    assertEquals(expectedUsers.get(0), result.get(0));

    verify(sessionFactory).getCurrentSession();
    verify(session).createQuery("SELECT entity FROM User entity ORDER BY entity.id", User.class);
    verify(query).getResultList();
  }
}