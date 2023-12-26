package com.epam.repository;

import com.epam.entity.Trainee;
import com.epam.entity.Training;
import com.epam.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

class TraineeRepositoryTest {

  @Mock
  private SessionFactory sessionFactory;

  @Mock
  private Session session;

  @Mock
  private Query<Trainee> query;

  @InjectMocks
  private TraineeRepository traineeRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testGetByUsername() {
    String username = "testUsername";
    Trainee expectedTrainee = new Trainee();

    when(sessionFactory.getCurrentSession()).thenReturn(session);
    Query<Trainee> mockQuery = mock(Query.class);
    when(session.createQuery(anyString(), eq(Trainee.class))).thenReturn(mockQuery);
    when(mockQuery.setParameter("username", username)).thenReturn(mockQuery);
    when(mockQuery.uniqueResultOptional()).thenReturn(Optional.of(expectedTrainee));

    Optional<Trainee> result = traineeRepository.getByUsername(username);

    assertTrue(result.isPresent());
    assertEquals(expectedTrainee, result.get());

    verify(sessionFactory).getCurrentSession();
    verify(session).createQuery(anyString(), eq(Trainee.class));
    verify(mockQuery).setParameter("username", username);
    verify(mockQuery).uniqueResultOptional();
  }

  @Test
  public void testChangePassword() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Long id = 1L;
    String password = "newPassword";
    Trainee trainee = new Trainee();
    trainee.setId(id);
    trainee.setUser(new User());

    Method toggleActivation = EntityWithUserRepository.class.getDeclaredMethod("toggleActivation", Long.class, Boolean.class);
    toggleActivation.setAccessible(true);
    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.merge(any())).thenReturn(trainee);
    when(session.get(eq(Trainee.class), anyLong())).thenReturn(trainee);

    Trainee updatedTrainee = traineeRepository.changePassword(id, password);

    assertEquals(password, updatedTrainee.getUser().getPassword());
    verify(sessionFactory, times(2)).getCurrentSession();
    verify(session).merge(any());
    verify(session).get(eq(Trainee.class), anyLong());
  }
  @Test
  public void testActivate() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Long id = 1L;
    Trainee trainee = new Trainee();
    trainee.setId(id);
    trainee.setUser(new User());

    Method toggleActivation = EntityWithUserRepository.class.getDeclaredMethod("toggleActivation", Long.class, Boolean.class);
    toggleActivation.setAccessible(true);

    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.merge(any())).thenReturn(trainee);
    when(session.get(eq(Trainee.class), anyLong())).thenReturn(trainee);

    Trainee activatedTrainee = traineeRepository.activate(id);

    assertEquals(true, activatedTrainee.getUser().getIsActive());

    verify(sessionFactory, times(2)).getCurrentSession();
    verify(session).merge(any());
  }

  @Test
  public void testDeactivate() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Long id = 1L;
    Trainee trainee = new Trainee();
    trainee.setId(id);
    trainee.setUser(new User());

    Method toggleActivation = EntityWithUserRepository.class.getDeclaredMethod("toggleActivation", Long.class, Boolean.class);
    toggleActivation.setAccessible(true);

    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.merge(any())).thenReturn(trainee);
    when(session.get(eq(Trainee.class), anyLong())).thenReturn(trainee);

    Trainee deactivatedTrainee = traineeRepository.deactivate(id);

    assertEquals(false, deactivatedTrainee.getUser().getIsActive());

    verify(sessionFactory, times(2)).getCurrentSession();
    verify(session).merge(any());
  }

  @Test
  public void testToggleActivationNonIdempotent() throws NoSuchMethodException {
    Long id = 1L;
    Trainee trainee = new Trainee();
    User user = new User();
    user.setIsActive(false);
    trainee.setId(id);
    trainee.setUser(user);

    Method toggleActivation = EntityWithUserRepository.class.getDeclaredMethod("toggleActivation", Long.class, Boolean.class);
    toggleActivation.setAccessible(true);

    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.merge(any())).thenReturn(trainee);
    when(session.get(eq(Trainee.class), anyLong())).thenReturn(trainee);

    Trainee toggledTrainee = traineeRepository.toggleActivationNonIdempotent(id);

    assertTrue(toggledTrainee.getUser().getIsActive());

    verify(sessionFactory, times(2)).getCurrentSession();
    verify(session).merge(any());
  }
}