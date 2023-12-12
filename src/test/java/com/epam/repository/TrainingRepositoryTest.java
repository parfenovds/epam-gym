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
    // Подготовка тестовых данных
    Long trainingId = 1L;
    Training testTraining = new Training();
    testTraining.setId(trainingId);

    // Устанавливаем поведение хранилища
    when(storage.getTrainings()).thenReturn(Map.of(trainingId, testTraining));

    // Выполняем тестирование
    Training retrievedTraining = trainingRepository.get(trainingId);

    // Проверяем, что результат соответствует ожиданиям
    assertNotNull(retrievedTraining);
    assertEquals(trainingId, retrievedTraining.getId());
  }

  @Test
  void testCreateTraining() {
    // Подготовка тестовых данных
    Training testTraining = new Training();

    // Выполняем тестирование
    trainingRepository.create(testTraining);

    // Проверяем, что методы хранилища были вызваны
    verify(storage).getNextTrainingId();
    verify(storage.getTrainings()).put(any(Long.class), eq(testTraining));
  }

  @Test
  void testGetTrainingsByField() {
    // Подготовка тестовых данных
    Long idToMatch = 1L;
    Training training1 = new Training();
    training1.setId(1L);
    Training training2 = new Training();
    training2.setId(2L);
    List<Training> trainings = List.of(training1, training2);

    // Устанавливаем поведение хранилища
    when(storage.getTrainings()).thenReturn(Map.of(1L, training1, 2L, training2));

    // Выполняем тестирование
    List<Training> retrievedTrainings = trainingRepository.getTrainingsByField(idToMatch, Training::getId);

    // Проверяем, что результат соответствует ожиданиям
    assertNotNull(retrievedTrainings);
    assertEquals(1, retrievedTrainings.size());
    assertEquals(training1.getId(), retrievedTrainings.get(0).getId());
  }

  @Test
  void testUpdateTraining() {
    // Подготовка тестовых данных
    Long trainingId = 1L;
    Training updatedTraining = new Training();
    updatedTraining.setId(trainingId);

    // Выполняем тестирование
    trainingRepository.update(updatedTraining);

    // Проверяем, что метод хранилища был вызван
    verify(storage.getTrainings()).put(trainingId, updatedTraining);
  }

  @Test
  void testDeleteTraining() {
    // Подготовка тестовых данных
    Long trainingId = 1L;

    // Устанавливаем поведение хранилища
    when(storage.getTrainings()).thenReturn(mock(Map.class));

    // Выполняем тестирование
    trainingRepository.delete(trainingId);

    // Проверяем, что метод хранилища был вызван
    verify(storage.getTrainings()).remove(trainingId);
  }
}