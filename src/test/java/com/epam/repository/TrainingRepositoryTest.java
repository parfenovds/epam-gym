package com.epam.repository;

import com.epam.entity.Training;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class TrainingRepositoryTest {

  @Mock
  private SessionFactory sessionFactory;

  @Mock
  private Session session;

  @InjectMocks
  private TrainingRepository trainingRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    when(sessionFactory.getCurrentSession()).thenReturn(session);
  }

  @Test
  void testGetById() {
    Long id = 1L;
    Training training = new Training();
    when(session.get(eq(Training.class), any(Long.class))).thenReturn(training);

    Optional<Training> result = trainingRepository.get(id);

    assertEquals(training, result.orElse(null));
    verify(session).get(Training.class, id);
  }

  @Test
  void testDelete() {
    Long id = 1L;
    Training training = new Training();
    when(session.get(eq(Training.class), any(Long.class))).thenReturn(training);

    trainingRepository.delete(id);

    verify(session).remove(training);
  }

  @Test
  void testUpdate() {
    Training training = new Training();

    trainingRepository.update(training);

    verify(session).merge(training);
  }

  @Test
  void testCreate() {
    Training training = new Training();

    trainingRepository.create(training);

    verify(session).persist(training);
  }

//  @Test
//  void testGetAll() {
//    List<Training> trainingList = List.of(new Training(), new Training());
//    when(session.createQuery(anyString(), eq(Training.class))).thenReturn(mock(org.hibernate.query.Query.class));
//    when(session.createQuery(anyString(), eq(Training.class)).getResultList()).thenReturn(trainingList);
//
//    List<Training> result = trainingRepository.getAll();
//
//    assertEquals(trainingList, result);
//    verify(session).createQuery("SELECT entity FROM Training entity ORDER BY entity.id", Training.class);
//  }
}