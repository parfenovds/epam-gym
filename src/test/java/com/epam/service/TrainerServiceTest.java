package com.epam.service;

import com.epam.entity.Trainer;
import com.epam.entity.Training;
import com.epam.entity.User;
import com.epam.repository.TrainerRepository;
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

class TrainerServiceTest {

  @Mock
  private TrainerRepository trainerRepository;

  @Mock
  private Validator validator;

  @InjectMocks
  private TrainerService trainerService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void save_ValidTrainer_ReturnsSavedTrainer() {
    Trainer trainer = new Trainer();
    trainer.setId(1L);
    trainer.setUser(new User());
    when(trainerRepository.create(trainer)).thenReturn(trainer);

    Trainer savedTrainer = trainerService.save(trainer);

    assertEquals(trainer, savedTrainer);
    verify(trainerRepository, times(1)).create(trainer);
  }

  @Test
  void findByUsername_ExistingUsername_ReturnsTrainer() {
    String username = "testUsername";
    Trainer trainer = new Trainer();
    trainer.setId(1L);
    trainer.setUser(new User());
    when(trainerRepository.getByUsername(username)).thenReturn(Optional.of(trainer));

    Trainer foundTrainer = trainerService.findByUsername(username);

    assertEquals(trainer, foundTrainer);
    verify(trainerRepository, times(1)).getByUsername(username);
  }

  @Test
  void changePassword_ValidTrainerIdAndPassword_ReturnsUpdatedTrainer() {
    Long trainerId = 1L;
    String newPassword = "newPassword";
    Trainer trainer = new Trainer();
    trainer.setId(trainerId);
    trainer.setUser(new User());
    when(trainerRepository.get(trainerId)).thenReturn(Optional.of(trainer));
    when(trainerRepository.update(trainer)).thenReturn(trainer);

    Trainer updatedTrainer = trainerService.changePassword(trainerId, newPassword);

    assertEquals(newPassword, updatedTrainer.getUser().getPassword());
    verify(trainerRepository, times(1)).update(trainer);
  }

  @Test
  void activate_ValidTrainerId_ReturnsActivatedTrainer() {
    Long trainerId = 1L;
    Trainer trainer = new Trainer();
    trainer.setId(trainerId);
    trainer.setUser(new User());
    when(trainerRepository.activate(trainerId)).thenReturn(trainer);

    Trainer activatedTrainer = trainerService.activate(trainerId);

    assertEquals(trainer, activatedTrainer);
    verify(trainerRepository, times(1)).activate(trainerId);
  }

  @Test
  void deactivate_ValidTrainerId_ReturnsDeactivatedTrainer() {
    Long trainerId = 1L;
    Trainer trainer = new Trainer();
    trainer.setId(trainerId);
    trainer.setUser(new User());
    when(trainerRepository.deactivate(trainerId)).thenReturn(trainer);

    Trainer deactivatedTrainer = trainerService.deactivate(trainerId);

    assertEquals(trainer, deactivatedTrainer);
    verify(trainerRepository, times(1)).deactivate(trainerId);
  }

  @Test
  void toggleActivationNonIdempotent_ValidTrainerId_ReturnsToggledTrainer() {
    Long trainerId = 1L;
    Trainer trainer = new Trainer();
    trainer.setId(trainerId);
    trainer.setUser(new User());
    when(trainerRepository.toggleActivationNonIdempotent(trainerId)).thenReturn(trainer);

    Trainer toggledTrainer = trainerService.toggleActivationNonIdempotent(trainerId);

    assertEquals(trainer, toggledTrainer);
    verify(trainerRepository, times(1)).toggleActivationNonIdempotent(trainerId);
  }

  @Test
  void trainingsByUsernameAndCriteria_ValidUsernameAndCriteria_ReturnsTrainings() {
    String username = "testUsername";
    String property = "trainingTypeProperty";
    String value = "trainingTypeValue";
    List<Training> trainings = Collections.singletonList(new Training());
    when(trainerRepository.trainingsByUsernameAndCriteria(username, property, value)).thenReturn(trainings);

    List<Training> result = trainerService.trainingsByUsernameAndCriteria(username, property, value);

    assertEquals(trainings, result);
    verify(trainerRepository, times(1)).trainingsByUsernameAndCriteria(username, property, value);
  }

  @Test
  void listOfNotAssignedToTrainee_ValidTraineeId_ReturnsListOfTrainers() {
    Long traineeId = 1L;
    List<Trainer> trainers = Collections.singletonList(new Trainer());
    when(trainerRepository.listOfNotAssignedToTrainee(traineeId)).thenReturn(trainers);

    List<Trainer> result = trainerService.listOfNotAssignedToTrainee(traineeId);

    assertEquals(trainers, result);
    verify(trainerRepository, times(1)).listOfNotAssignedToTrainee(traineeId);
  }
}
