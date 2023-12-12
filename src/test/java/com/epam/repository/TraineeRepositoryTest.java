package com.epam.repository;

import com.epam.commonDB.Storage;
import com.epam.entity.Trainee;
import com.epam.entity.Training;
import com.epam.entity.User;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class TraineeRepositoryTest {

  @Mock
  private Storage storage;

  @Mock
  private TrainingRepository trainingRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private TraineeRepository traineeRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    when(storage.getNextTraineeId()).thenReturn(new AtomicLong(1L));
    when(storage.getTrainees()).thenReturn(new HashMap<>());
  }

  @Test
  public void testGet() {
    User user = User.builder()
        .id(1L)
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .password("password")
        .isActive(true)
        .build();

    Trainee trainee = Trainee.builder()
        .id(1L)
        .dateOfBirth(LocalDate.of(1990, 5, 15))
        .address("123 Main St")
        .user(user)
        .trainings(new ArrayList<>())
        .build();

    Map<Long, Trainee> traineeMap = new HashMap<>();
    traineeMap.put(trainee.getId(), trainee);
    when(storage.getTrainees()).thenReturn(traineeMap);

    Trainee retrievedTrainee = traineeRepository.get(trainee.getId());

    assertEquals(trainee.getId(), retrievedTrainee.getId());
    assertEquals(trainee.getDateOfBirth(), retrievedTrainee.getDateOfBirth());
    assertEquals(trainee.getAddress(), retrievedTrainee.getAddress());
    assertEquals(trainee.getUser(), retrievedTrainee.getUser());
    assertEquals(trainee.getTrainings(), retrievedTrainee.getTrainings());
  }
  @Test
  public void testCreateTrainee() {
    User user = User.builder()
        .id(1L)
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .password("password")
        .isActive(true)
        .build();
    Trainee traineeToCreate = Trainee.builder()
        .dateOfBirth(LocalDate.of(1995, 3, 20))
        .address("456 Oak St")
        .user(user)
        .trainings(new ArrayList<>())
        .build();

    when(userRepository.create(any(User.class))).thenReturn(traineeToCreate.getUser());

    Trainee createdTrainee = traineeRepository.create(traineeToCreate);

    assertNotNull(createdTrainee);
    assertEquals(traineeToCreate.getDateOfBirth(), createdTrainee.getDateOfBirth());
    assertEquals(traineeToCreate.getAddress(), createdTrainee.getAddress());
  }


  @Test
  public void testUpdate() {
    Trainee existingTrainee = Trainee.builder()
        .id(1L)
        .dateOfBirth(LocalDate.of(1990, 5, 15))
        .address("123 Main St")
        .user(User.builder().id(1L).build())
        .trainings(new ArrayList<>())
        .build();

    Map<Long, Trainee> traineesMap = new HashMap<>();
    traineesMap.put(existingTrainee.getId(), existingTrainee);

    when(storage.getTrainees()).thenReturn(traineesMap);

    Trainee updatedTrainee = Trainee.builder()
        .id(existingTrainee.getId())
        .dateOfBirth(LocalDate.of(1992, 10, 8))
        .address("789 Elm St")
        .user(User.builder().id(1L).build())
        .trainings(new ArrayList<>())
        .build();

    Trainee result = traineeRepository.update(updatedTrainee);

    assertEquals(updatedTrainee.getId(), result.getId());
    assertEquals(updatedTrainee.getDateOfBirth(), result.getDateOfBirth());
    assertEquals(updatedTrainee.getAddress(), result.getAddress());
    assertEquals(updatedTrainee.getUser(), result.getUser());
    assertEquals(updatedTrainee.getTrainings(), result.getTrainings());
  }

  @Test
  public void testDelete() {
    Storage storage = new Storage();

    Long traineeId = 1L;
    Trainee traineeToDelete = Trainee.builder()
        .id(traineeId)
        .dateOfBirth(LocalDate.of(1990, 5, 15))
        .address("123 Main St")
        .user(User.builder().id(1L).build())
        .trainings(new ArrayList<>())
        .build();

    storage.getTrainees().put(traineeToDelete.getId(), traineeToDelete);
    storage.getNextTraineeId().set(2L); // Задаем следующее ожидаемое значение
    TraineeRepository traineeRepository = new TraineeRepository(storage, trainingRepository, userRepository);
    traineeRepository.delete(traineeId);
    assertFalse(storage.getTrainees().containsKey(traineeId));
  }

  @Test
  public void testGetAll() {
    Map<Long, Trainee> trainees = new HashMap<>();
    trainees.put(1L, new Trainee());
    trainees.put(2L, new Trainee());
    when(storage.getTrainees()).thenReturn(trainees);

    Collection<Trainee> allTrainees = traineeRepository.getAll();

    assertEquals(2, allTrainees.size()); // Проверка размера коллекции
  }

  @Test
  public void testCreateTrainings() throws Exception {
    Trainee trainee = Trainee.builder()
        .id(1L)
        .dateOfBirth(LocalDate.of(1990, 5, 15))
        .address("123 Main St")
        .user(User.builder().id(1L).build())
        .trainings(new ArrayList<>())
        .build();

    Training training1 = Training.builder()
        .id(1L)
        .traineeId(trainee.getId())
        .trainerId(2L)
        .trainingName("Training 1")
        .trainingTypeId(3L)
        .trainingDate(LocalDate.now())
        .trainingDuration(60L)
        .build();

    Training training2 = Training.builder()
        .id(2L)
        .traineeId(trainee.getId())
        .trainerId(3L)
        .trainingName("Training 2")
        .trainingTypeId(4L)
        .trainingDate(LocalDate.now())
        .trainingDuration(45L)
        .build();

    trainee.getTrainings().add(training1);
    trainee.getTrainings().add(training2);

    Method createTrainingsMethod = TraineeRepository.class.getDeclaredMethod("createTrainings", Trainee.class);
    createTrainingsMethod.setAccessible(true);

    TrainingRepository trainingRepository = mock(TrainingRepository.class);

    UserRepository userRepository = mock(UserRepository.class);
    when(userRepository.create(any())).thenReturn(User.builder().id(1L).build());

    TraineeRepository traineeRepository = new TraineeRepository(storage, trainingRepository, userRepository);

    createTrainingsMethod.invoke(traineeRepository, trainee);

    verify(trainingRepository).create(training1);
    verify(trainingRepository).create(training2);
  }

  @Test
  public void testValidateEntity() throws Exception {
    TraineeRepository traineeRepository = new TraineeRepository(storage, trainingRepository, userRepository);

    Method validateEntityMethod = TraineeRepository.class.getDeclaredMethod("validateEntity", Trainee.class);
    validateEntityMethod.setAccessible(true);

    Trainee trainee = Trainee.builder()
        .id(1L)
        .dateOfBirth(LocalDate.of(1990, 5, 15))
        .address("123 Main St")
        .trainings(new ArrayList<>())
        .build();

    try {
      validateEntityMethod.invoke(traineeRepository, trainee);
      fail("Expected IllegalArgumentException was not thrown");
    } catch (InvocationTargetException e) {
      assertTrue(e.getCause() instanceof IllegalArgumentException);
      assertEquals("Trainee must have a user", e.getCause().getMessage());
    }
  }

  @Test
  public void testSetUserIfNull() throws Exception {
    TraineeRepository traineeRepository = new TraineeRepository(storage, trainingRepository, userRepository);

    Method setUserIfNullMethod = TraineeRepository.class.getDeclaredMethod("setUserIfNull", Trainee.class);
    setUserIfNullMethod.setAccessible(true);

    Trainee trainee = Trainee.builder()
        .id(1L)
        .dateOfBirth(LocalDate.of(1990, 5, 15))
        .address("123 Main St")
        .trainings(new ArrayList<>())
        .build();

    UserRepository mockedUserRepository = mock(UserRepository.class);

    Field userRepositoryField = TraineeRepository.class.getDeclaredField("userRepository");
    userRepositoryField.setAccessible(true);
    userRepositoryField.set(traineeRepository, mockedUserRepository);

    setUserIfNullMethod.invoke(traineeRepository, trainee);

    verify(mockedUserRepository).create(null);
  }
}
