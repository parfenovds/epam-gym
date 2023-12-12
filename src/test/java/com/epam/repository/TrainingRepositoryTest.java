package com.epam.repository;

import com.epam.commonDB.Storage;
import com.epam.entity.Training;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

class TrainingRepositoryTest {

  @Mock
  private Storage storage;

  @InjectMocks
  private TrainingRepository trainingRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    when(storage.getNextTrainingId()).thenReturn(new AtomicLong(1L));
    when(storage.getTrainings()).thenReturn(mock(Map.class));
  }

  @Test
  void testGetById() {
    Long trainingId = 1L;
    Training testTraining = new Training();
    testTraining.setId(trainingId);

    when(storage.getTrainings()).thenReturn(Map.of(trainingId, testTraining));

    Training retrievedTraining = trainingRepository.get(trainingId);

    assertNotNull(retrievedTraining);
    assertEquals(trainingId, retrievedTraining.getId());
  }

  @Test
  void testCreateTraining() {
    Training testTraining = new Training();

    trainingRepository.create(testTraining);

    verify(storage).getNextTrainingId();
    verify(storage.getTrainings()).put(any(Long.class), eq(testTraining));
  }

  @Test
  void testGetTrainingsByField() {
    Long idToMatch = 1L;
    Training training1 = new Training();
    training1.setId(1L);
    Training training2 = new Training();
    training2.setId(2L);
    List<Training> trainings = List.of(training1, training2);

    when(storage.getTrainings()).thenReturn(Map.of(1L, training1, 2L, training2));

    List<Training> retrievedTrainings = trainingRepository.getTrainingsByField(idToMatch, Training::getId);

    assertNotNull(retrievedTrainings);
    assertEquals(1, retrievedTrainings.size());
    assertEquals(training1.getId(), retrievedTrainings.get(0).getId());
  }

  @Test
  void testUpdateTraining() {
    Long trainingId = 1L;
    Training updatedTraining = new Training();
    updatedTraining.setId(trainingId);

    trainingRepository.update(updatedTraining);

    verify(storage.getTrainings()).put(trainingId, updatedTraining);
  }

  @Test
  void testDeleteTraining() {
    Long trainingId = 1L;

    when(storage.getTrainings()).thenReturn(mock(Map.class));

    trainingRepository.delete(trainingId);

    verify(storage.getTrainings()).remove(trainingId);
  }
}
