package com.epam.repository;

import com.epam.entity.Trainer;
import com.epam.entity.User;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

public class TrainerRepositoryTest {

  @Mock
  private SessionFactory sessionFactory;

  @Mock
  private Session session;

  @Mock
  private Query<Trainer> query;

  @InjectMocks
  private TrainerRepository trainerRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testGetByUsername() {
    String username = "testUsername";
    Trainer expectedTrainer = new Trainer();

    when(sessionFactory.getCurrentSession()).thenReturn(session);
    Query<Trainer> mockQuery = mock(Query.class);
    when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(mockQuery);
    when(mockQuery.setParameter("username", username)).thenReturn(mockQuery);
    when(mockQuery.uniqueResultOptional()).thenReturn(Optional.of(expectedTrainer));

    Optional<Trainer> result = trainerRepository.getByUsername(username);

    assertTrue(result.isPresent());
    assertEquals(expectedTrainer, result.get());

    verify(sessionFactory).getCurrentSession();
    verify(session).createQuery(anyString(), eq(Trainer.class));
    verify(mockQuery).setParameter("username", username);
    verify(mockQuery).uniqueResultOptional();
  }

  @Test
  public void testChangePassword() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Long id = 1L;
    String password = "newPassword";
    Trainer trainer = new Trainer();
    trainer.setId(id);
    trainer.setUser(new User());

    Method toggleActivation = EntityWithUserRepository.class.getDeclaredMethod("toggleActivation", Long.class, Boolean.class);
    toggleActivation.setAccessible(true);
    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.merge(any())).thenReturn(trainer);
    when(session.get(eq(Trainer.class), anyLong())).thenReturn(trainer);

    Trainer updatedTrainer = trainerRepository.changePassword(id, password);

    assertEquals(password, updatedTrainer.getUser().getPassword());
    verify(sessionFactory, times(2)).getCurrentSession();
    verify(session).merge(any());
    verify(session).get(eq(Trainer.class), anyLong());
  }

  @Test
  public void testActivate() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Long id = 1L;
    Trainer trainer = new Trainer();
    trainer.setId(id);
    trainer.setUser(new User());

    Method toggleActivation = EntityWithUserRepository.class.getDeclaredMethod("toggleActivation", Long.class, Boolean.class);
    toggleActivation.setAccessible(true);

    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.merge(any())).thenReturn(trainer);
    when(session.get(eq(Trainer.class), anyLong())).thenReturn(trainer);

    Trainer activatedTrainer = trainerRepository.activate(id);

    assertEquals(true, activatedTrainer.getUser().getIsActive());

    verify(sessionFactory, times(2)).getCurrentSession();
    verify(session).merge(any());
  }

  @Test
  public void testDeactivate() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Long id = 1L;
    Trainer trainer = new Trainer();
    trainer.setId(id);
    trainer.setUser(new User());

    Method toggleActivation = EntityWithUserRepository.class.getDeclaredMethod("toggleActivation", Long.class, Boolean.class);
    toggleActivation.setAccessible(true);

    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.merge(any())).thenReturn(trainer);
    when(session.get(eq(Trainer.class), anyLong())).thenReturn(trainer);

    Trainer deactivatedTrainer = trainerRepository.deactivate(id);

    assertEquals(false, deactivatedTrainer.getUser().getIsActive());

    verify(sessionFactory, times(2)).getCurrentSession();
    verify(session).merge(any());
  }

  @Test
  public void testToggleActivationNonIdempotent() throws NoSuchMethodException {
    Long id = 1L;
    Trainer trainer = new Trainer();
    User user = new User();
    user.setIsActive(false);
    trainer.setId(id);
    trainer.setUser(user);

    Method toggleActivation = EntityWithUserRepository.class.getDeclaredMethod("toggleActivation", Long.class, Boolean.class);
    toggleActivation.setAccessible(true);

    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.merge(any())).thenReturn(trainer);
    when(session.get(eq(Trainer.class), anyLong())).thenReturn(trainer);

    Trainer toggledTrainer = trainerRepository.toggleActivationNonIdempotent(id);

    assertTrue(toggledTrainer.getUser().getIsActive());

    verify(sessionFactory, times(2)).getCurrentSession();
    verify(session).merge(any());
  }

  @Test
  public void testListOfNotAssignedToTrainee() {
    Long traineeId = 1L;
    List<Trainer> expectedTrainers = Collections.singletonList(new Trainer());

    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
    when(query.setParameter("traineeId", traineeId)).thenReturn(query);
    when(query.getResultList()).thenReturn(expectedTrainers);

    List<Trainer> trainers = trainerRepository.listOfNotAssignedToTrainee(traineeId);

    assertEquals(expectedTrainers.size(), trainers.size());

    verify(sessionFactory).getCurrentSession();
    verify(session).createQuery(anyString(), eq(Trainer.class));
    verify(query).setParameter("traineeId", traineeId);
    verify(query).getResultList();
  }
}
