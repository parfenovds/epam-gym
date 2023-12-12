package com.epam.service;

import com.epam.entity.Trainee;
import com.epam.entity.User;
import com.epam.repository.TraineeRepository;
import java.time.LocalDate;
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

public class TraineeServiceTest {

  @Mock
  private TraineeRepository traineeRepository;

  @InjectMocks
  private TraineeService traineeService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testFindById() {
    Long traineeId = 1L;
    Trainee trainee = Trainee.builder()
        .id(traineeId)
        .dateOfBirth(LocalDate.of(1990, 5, 15))
        .address("123 Main St")
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

    when(traineeRepository.get(traineeId)).thenReturn(trainee);

    Trainee foundTrainee = traineeService.findById(traineeId);

    verify(traineeRepository, times(1)).get(traineeId);
    assertSame(trainee, foundTrainee);
  }

  @Test
  public void testSave() {
    Trainee traineeToSave = Trainee.builder()
        .id(1L)
        .dateOfBirth(LocalDate.of(1990, 5, 15))
        .address("123 Main St")
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

    when(traineeRepository.create(traineeToSave)).thenReturn(traineeToSave);

    Trainee savedTrainee = traineeService.save(traineeToSave);

    verify(traineeRepository, times(1)).create(traineeToSave);
    assertSame(traineeToSave, savedTrainee);
  }

  @Test
  public void testUpdate() {
    Trainee traineeToUpdate = Trainee.builder()
        .id(1L)
        .dateOfBirth(LocalDate.of(1990, 5, 15))
        .address("123 Main St")
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

    when(traineeRepository.update(traineeToUpdate)).thenReturn(traineeToUpdate);

    Trainee updatedTrainee = traineeService.update(traineeToUpdate);

    verify(traineeRepository, times(1)).update(traineeToUpdate);
    assertSame(traineeToUpdate, updatedTrainee);
  }

  @Test
  public void testDelete() {
    Long traineeIdToDelete = 1L;

    traineeService.delete(traineeIdToDelete);

    verify(traineeRepository, times(1)).delete(traineeIdToDelete);
  }
}