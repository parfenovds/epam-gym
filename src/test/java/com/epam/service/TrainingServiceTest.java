package com.epam.service;

import com.epam.entity.Training;
import com.epam.repository.BaseRepository;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

  @Mock
  private BaseRepository<Training> trainingRepository;

  @Mock
  private Validator validator;

  @InjectMocks
  private TrainingService trainingService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void save_ValidTraining_CreatesTraining() {
    Training training = new Training();
    // Set valid fields for the training

    when(validator.validate(training)).thenReturn(Collections.emptySet());
    when(trainingRepository.create(training)).thenReturn(training);

    Training savedTraining = trainingService.save(training);

    assertNotNull(savedTraining);
    assertEquals(training, savedTraining);
    verify(trainingRepository, times(1)).create(training);
  }

  @Test
  void save_InvalidTraining_ThrowsException() {
    Training training = new Training();
    // Set invalid fields for the training

    Set<ConstraintViolation<Training>> violations = Collections.singleton(mock(ConstraintViolation.class));
    when(validator.validate(training)).thenReturn(violations);

    assertThrows(IllegalArgumentException.class, () -> trainingService.save(training));
    verifyNoInteractions(trainingRepository);
  }

  @Test
  void findById_ExistingId_ReturnsTraining() {
    Long trainingId = 1L;
    Training training = new Training();
    training.setId(trainingId);

    when(trainingRepository.get(trainingId)).thenReturn(Optional.of(training));

    Training foundTraining = trainingService.findById(trainingId);

    assertNotNull(foundTraining);
    assertEquals(trainingId, foundTraining.getId());
    verify(trainingRepository, times(1)).get(trainingId);
  }

  @Test
  void findById_NonExistingId_ThrowsException() {
    Long trainingId = 1L;

    when(trainingRepository.get(trainingId)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> trainingService.findById(trainingId));
    verify(trainingRepository, times(1)).get(trainingId);
  }

  @Test
  void delete_ExistingId_DeletesTraining() {
    Long trainingId = 1L;

    trainingService.delete(trainingId);

    verify(trainingRepository, times(1)).delete(trainingId);
  }

  @Test
  void update_ValidTraining_UpdatesTraining() {
    Training training = new Training();
    // Set valid fields for the training

    when(validator.validate(training)).thenReturn(Collections.emptySet());
    when(trainingRepository.update(training)).thenReturn(training);

    Training updatedTraining = trainingService.update(training);

    assertNotNull(updatedTraining);
    assertEquals(training, updatedTraining);
    verify(trainingRepository, times(1)).update(training);
  }

  @Test
  void update_InvalidTraining_ThrowsException() {
    Training training = new Training();
    // Set invalid fields for the training

    Set<ConstraintViolation<Training>> violations = Collections.singleton(mock(ConstraintViolation.class));
    when(validator.validate(training)).thenReturn(violations);

    assertThrows(IllegalArgumentException.class, () -> trainingService.update(training));
    verifyNoInteractions(trainingRepository);
  }

  @Test
  void findAll_TrainingsExist_ReturnsTrainings() {
    Training training1 = new Training();
    Training training2 = new Training();
    // Set valid fields for the trainings

    when(trainingRepository.getAll()).thenReturn(List.of(training1, training2));

    Collection<Training> foundTrainings = trainingService.findAll();

    assertNotNull(foundTrainings);
    assertEquals(2, foundTrainings.size());
    assertTrue(foundTrainings.contains(training1));
    assertTrue(foundTrainings.contains(training2));
    verify(trainingRepository, times(1)).getAll();
  }
}