package com.epam.service;

import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.Training;
import com.epam.entity.User;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FacadeServiceTest {

  @Mock
  private TraineeService traineeService;

  @Mock
  private TrainerService trainerService;

  @Mock
  private TrainingService trainingService;

  @Mock
  private UserService userService;

  @InjectMocks
  private FacadeService facadeService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    facadeService = new FacadeService(traineeService, trainerService, trainingService, userService);
  }

  @Test
  void testFindTraineeById() {
    Long id = 1L;
    Trainee trainee = new Trainee();
    when(traineeService.findById(id)).thenReturn(trainee);

    Trainee result = facadeService.findTraineeById(id);

    assertEquals(trainee, result);
  }

  // Тесты для других методов FacadeService

  @Test
  void testTrainingsByUsernameOfTraineeAndCriteria() {
    String username = "testUser";
    String property = "property";
    String value = "value";
    List<Training> expectedTrainings = List.of(new Training(), new Training());
    when(traineeService.trainingsByUsernameAndCriteria(username, property, value)).thenReturn(expectedTrainings);

    List<Training> result = facadeService.trainingsByUsernameOfTraineeAndCriteria(username, property, value);

    assertEquals(expectedTrainings, result);
  }

  @Test
  void testToggleActivationNonIdempotent() {
    Long id = 1L;
    Trainee trainee = new Trainee();
    when(traineeService.toggleActivationNonIdempotent(id)).thenReturn(trainee);

    Trainee result = facadeService.toggleActivationNonIdempotent(id);

    assertEquals(trainee, result);
  }

  @Test
  public void testSaveTrainee() {
    // Arrange
    Trainee traineeToSave = new Trainee();
    Trainee savedTrainee = new Trainee();
    when(traineeService.save(traineeToSave)).thenReturn(savedTrainee);

    // Act
    Trainee result = facadeService.saveTrainee(traineeToSave);

    // Assert
    assertEquals(savedTrainee, result);
    verify(traineeService).save(traineeToSave);
  }

  @Test
  public void testUpdateTrainee() {
    // Arrange
    Trainee traineeToUpdate = new Trainee();
    Trainee updatedTrainee = new Trainee();
    when(traineeService.update(traineeToUpdate)).thenReturn(updatedTrainee);

    // Act
    Trainee result = facadeService.updateTrainee(traineeToUpdate);

    // Assert
    assertEquals(updatedTrainee, result);
    verify(traineeService).update(traineeToUpdate);
  }

  @Test
  public void testDeleteTrainee() {
    // Arrange
    Long traineeId = 1L;

    // Act
    facadeService.deleteTrainee(traineeId);

    // Assert
    verify(traineeService).delete(traineeId);
  }

  @Test
  public void testFindTrainerById() {
    // Arrange
    Long trainerId = 1L;
    Trainer expectedTrainer = new Trainer();
    when(trainerService.findById(trainerId)).thenReturn(expectedTrainer);

    // Act
    Trainer result = facadeService.findTrainerById(trainerId);

    // Assert
    assertEquals(expectedTrainer, result);
    verify(trainerService).findById(trainerId);
  }

  @Test
  public void testSaveTrainer() {
    // Arrange
    Trainer trainerToSave = new Trainer();
    Trainer savedTrainer = new Trainer();
    when(trainerService.save(trainerToSave)).thenReturn(savedTrainer);

    // Act
    Trainer result = facadeService.saveTrainer(trainerToSave);

    // Assert
    assertEquals(savedTrainer, result);
    verify(trainerService).save(trainerToSave);
  }

  @Test
  public void testUpdateTrainer() {
    // Arrange
    Trainer trainerToUpdate = new Trainer();
    Trainer updatedTrainer = new Trainer();
    when(trainerService.update(trainerToUpdate)).thenReturn(updatedTrainer);

    // Act
    Trainer result = facadeService.updateTrainer(trainerToUpdate);

    // Assert
    assertEquals(updatedTrainer, result);
    verify(trainerService).update(trainerToUpdate);
  }

  @Test
  public void testFindTrainingById() {
    // Arrange
    Long trainingId = 1L;
    Training expectedTraining = new Training();
    when(trainingService.findById(trainingId)).thenReturn(expectedTraining);

    // Act
    Training result = facadeService.findTrainingById(trainingId);

    // Assert
    assertEquals(expectedTraining, result);
    verify(trainingService).findById(trainingId);
  }

  @Test
  public void testSaveTraining() {
    // Arrange
    Training trainingToSave = new Training();
    Training savedTraining = new Training();
    when(trainingService.save(trainingToSave)).thenReturn(savedTraining);

    // Act
    Training result = facadeService.saveTraining(trainingToSave);

    // Assert
    assertEquals(savedTraining, result);
    verify(trainingService).save(trainingToSave);
  }

  @Test
  public void testFindAllUsers() {
    // Arrange
    Collection<User> expectedUsers = Arrays.asList(new User(), new User());
    when(userService.findAll()).thenReturn(expectedUsers);

    // Act
    Collection<User> result = facadeService.findAllUsers();

    // Assert
    assertEquals(expectedUsers, result);
    verify(userService).findAll();
  }

  @Test
  public void testFindAllTrainees() {
    // Arrange
    Collection<Trainee> expectedTrainees = Arrays.asList(new Trainee(), new Trainee());
    when(traineeService.findAll()).thenReturn(expectedTrainees);

    // Act
    Collection<Trainee> result = facadeService.findAllTrainees();

    // Assert
    assertEquals(expectedTrainees, result);
    verify(traineeService).findAll();
  }

  @Test
  public void testListOfNotAssignedToTrainee() {
    // Arrange
    Long traineeId = 1L;
    List<Trainer> expectedTrainers = Arrays.asList(new Trainer(), new Trainer());
    when(trainerService.listOfNotAssignedToTrainee(traineeId)).thenReturn(expectedTrainers);

    // Act
    List<Trainer> result = facadeService.listOfNotAssignedToTrainee(traineeId);

    // Assert
    assertEquals(expectedTrainers, result);
    verify(trainerService).listOfNotAssignedToTrainee(traineeId);
  }

  @Test
  public void testUsernameToPasswordMatchingCheck() {
    // Arrange
    String username = "testUser";
    String password = "testPassword";
    when(userService.usernameToPasswordMatchingCheck(username, password)).thenReturn(true);

    // Act
    boolean result = facadeService.usernameToPasswordMatchingCheck(username, password);

    // Assert
    assertTrue(result);
    verify(userService).usernameToPasswordMatchingCheck(username, password);
  }

  @Test
  public void testTrainingsByUsernameOfTrainerAndCriteria() {
    // Arrange
    String username = "testUser";
    String property = "property";
    String value = "value";
    List<Training> expectedTrainings = Arrays.asList(new Training(), new Training());
    when(trainerService.trainingsByUsernameAndCriteria(username, property, value)).thenReturn(expectedTrainings);

    // Act
    List<Training> result = facadeService.trainingsByUsernameOfTrainerAndCriteria(username, property, value);

    // Assert
    assertEquals(expectedTrainings, result);
    verify(trainerService).trainingsByUsernameAndCriteria(username, property, value);
  }

  @Test
  public void testToggleActivationNonIdempotentTrainer() {
    // Arrange
    Long trainerId = 1L;
    Trainer trainer = new Trainer();
    when(trainerService.toggleActivationNonIdempotent(trainerId)).thenReturn(trainer);

    // Act
    Trainer result = facadeService.toggleActivationNonIdempotentTrainer(trainerId);

    // Assert
    assertEquals(trainer, result);
    verify(trainerService).toggleActivationNonIdempotent(trainerId);
  }

  // Добавьте другие тесты для остальных методов FacadeService
}