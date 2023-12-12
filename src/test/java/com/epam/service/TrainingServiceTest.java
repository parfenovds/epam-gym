package com.epam.service;

import com.epam.entity.Training;
import com.epam.repository.TrainingRepository;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class TrainingServiceTest {

  @Mock
  private TrainingRepository trainingRepository;

  @InjectMocks
  private TrainingService trainingService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testFindById() {
    Long trainingId = 1L;
    Training training = Training.builder()
        .id(trainingId)
        .traineeId(1L)
        .trainerId(2L)
        .trainingName("Java Basics")
        .trainingTypeId(3L)
        .trainingDate(LocalDate.of(2023, 9, 15))
        .trainingDuration(5L)
        .build();

    when(trainingRepository.get(trainingId)).thenReturn(training);

    Training foundTraining = trainingService.findById(trainingId);

    verify(trainingRepository, times(1)).get(trainingId);
    assertSame(training, foundTraining);
  }

  @Test
  public void testSave() {
    Training trainingToSave = Training.builder()
        .traineeId(1L)
        .trainerId(2L)
        .trainingName("Python Basics")
        .trainingTypeId(4L)
        .trainingDate(LocalDate.of(2023, 10, 20))
        .trainingDuration(6L)
        .build();

    when(trainingRepository.create(trainingToSave)).thenReturn(trainingToSave);

    Training savedTraining = trainingService.save(trainingToSave);

    verify(trainingRepository, times(1)).create(trainingToSave);
    assertSame(trainingToSave, savedTraining);
  }
}