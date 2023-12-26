package com.epam.service;

import com.epam.entity.Trainee;
import com.epam.entity.Training;
import com.epam.entity.User;
import com.epam.repository.TraineeRepository;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

  @Mock
  private TraineeRepository traineeRepository;

  @Mock
  private Validator validator;

  @InjectMocks
  private TraineeService traineeService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void save_ValidTrainee_ReturnsSavedTrainee() {
    Trainee trainee = new Trainee();
    trainee.setId(1L);
    trainee.setUser(new User());
    when(traineeRepository.create(trainee)).thenReturn(trainee);

    Trainee savedTrainee = traineeService.save(trainee);

    assertEquals(trainee, savedTrainee);
    verify(traineeRepository, times(1)).create(trainee);
  }

  @Test
  void findByUsername_ExistingUsername_ReturnsTrainee() {
    String username = "testUsername";
    Trainee trainee = new Trainee();
    trainee.setId(1L);
    trainee.setUser(new User());
    when(traineeRepository.getByUsername(username)).thenReturn(Optional.of(trainee));

    Trainee foundTrainee = traineeService.findByUsername(username);

    assertEquals(trainee, foundTrainee);
    verify(traineeRepository, times(1)).getByUsername(username);
  }

  @Test
  void changePassword_ValidTraineeIdAndPassword_ReturnsUpdatedTrainee() {
    Long traineeId = 1L;
    String newPassword = "newPassword";
    Trainee trainee = new Trainee();
    trainee.setId(traineeId);
    trainee.setUser(new User());
    when(traineeRepository.get(traineeId)).thenReturn(Optional.of(trainee));
    when(traineeRepository.update(trainee)).thenReturn(trainee);

    Trainee updatedTrainee = traineeService.changePassword(traineeId, newPassword);

    assertEquals(newPassword, updatedTrainee.getUser().getPassword());
    verify(traineeRepository, times(1)).update(trainee);
  }

  @Test
  void activate_ValidTraineeId_ReturnsActivatedTrainee() {
    Long traineeId = 1L;
    Trainee trainee = new Trainee();
    trainee.setId(traineeId);
    trainee.setUser(new User());
    when(traineeRepository.activate(traineeId)).thenReturn(trainee);

    Trainee activatedTrainee = traineeService.activate(traineeId);

    assertEquals(trainee, activatedTrainee);
    verify(traineeRepository, times(1)).activate(traineeId);
  }

  @Test
  void deactivate_ValidTraineeId_ReturnsDeactivatedTrainee() {
    Long traineeId = 1L;
    Trainee trainee = new Trainee();
    trainee.setId(traineeId);
    trainee.setUser(new User());
    when(traineeRepository.deactivate(traineeId)).thenReturn(trainee);

    Trainee deactivatedTrainee = traineeService.deactivate(traineeId);

    assertEquals(trainee, deactivatedTrainee);
    verify(traineeRepository, times(1)).deactivate(traineeId);
  }

  @Test
  void toggleActivationNonIdempotent_ValidTraineeId_ReturnsToggledTrainee() {
    Long traineeId = 1L;
    Trainee trainee = new Trainee();
    trainee.setId(traineeId);
    trainee.setUser(new User());
    when(traineeRepository.toggleActivationNonIdempotent(traineeId)).thenReturn(trainee);

    Trainee toggledTrainee = traineeService.toggleActivationNonIdempotent(traineeId);

    assertEquals(trainee, toggledTrainee);
    verify(traineeRepository, times(1)).toggleActivationNonIdempotent(traineeId);
  }

  @Test
  void trainingsByUsernameAndCriteria_ValidUsernameAndCriteria_ReturnsTrainings() {
    String username = "testUsername";
    String property = "trainingTypeProperty";
    String value = "trainingTypeValue";
    List<Training> trainings = Collections.singletonList(new Training());
    when(traineeRepository.trainingsByUsernameAndCriteria(username, property, value)).thenReturn(trainings);

    List<Training> result = traineeService.trainingsByUsernameAndCriteria(username, property, value);

    assertEquals(trainings, result);
    verify(traineeRepository, times(1)).trainingsByUsernameAndCriteria(username, property, value);
  }
}