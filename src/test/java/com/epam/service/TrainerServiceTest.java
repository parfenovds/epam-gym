package com.epam.service;

import com.epam.entity.Trainer;
import com.epam.entity.User;
import com.epam.repository.TrainerRepository;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class TrainerServiceTest {

  @Mock
  private TrainerRepository trainerRepository;

  @InjectMocks
  private TrainerService trainerService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testFindById() {
    Long trainerId = 1L;
    Trainer trainer = Trainer.builder()
        .id(trainerId)
        .trainingTypeId(2L)
        .user(User.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .username("johndoe")
            .password("password")
            .isActive(true)
            .build())
        .trainings(new ArrayList<>())
        .build();

    when(trainerRepository.get(trainerId)).thenReturn(trainer);

    Trainer foundTrainer = trainerService.findById(trainerId);

    verify(trainerRepository, times(1)).get(trainerId);
    assertSame(trainer, foundTrainer);
  }
  @Test
  public void testSave() {
    Trainer trainerToSave = Trainer.builder()
        .id(1L)
        .trainingTypeId(2L)
        .user(User.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .username("johndoe")
            .password("password")
            .isActive(true)
            .build())
        .trainings(new ArrayList<>())
        .build();

    when(trainerRepository.create(trainerToSave)).thenReturn(trainerToSave);

    Trainer savedTrainer = trainerService.save(trainerToSave);

    verify(trainerRepository, times(1)).create(trainerToSave);
    assertSame(trainerToSave, savedTrainer);
  }

  @Test
  public void testUpdate() {
    Trainer trainerToUpdate = Trainer.builder()
        .id(1L)
        .trainingTypeId(2L)
        .user(User.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .username("johndoe")
            .password("password")
            .isActive(true)
            .build())
        .trainings(new ArrayList<>())
        .build();

    when(trainerRepository.update(trainerToUpdate)).thenReturn(trainerToUpdate);

    Trainer updatedTrainer = trainerService.update(trainerToUpdate);

    verify(trainerRepository, times(1)).update(trainerToUpdate);
    assertSame(trainerToUpdate, updatedTrainer);
  }
}