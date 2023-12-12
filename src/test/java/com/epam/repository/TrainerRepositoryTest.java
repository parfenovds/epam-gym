package com.epam.repository;

import com.epam.commonDB.Storage;
import com.epam.entity.Trainer;
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

class TrainerRepositoryTest {
  @Mock
  private Storage storage;

  @Mock
  private TrainingRepository trainingRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private TrainerRepository trainerRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    when(storage.getNextTrainerId()).thenReturn(new AtomicLong(1L));
    when(storage.getTrainers()).thenReturn(new HashMap<>());
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

    Trainer trainer = Trainer.builder()
        .id(1L)
        .trainingTypeId(2L)
        .user(user)
        .trainings(new ArrayList<>())
        .build();

    Map<Long, Trainer> trainerMap = new HashMap<>();
    trainerMap.put(trainer.getId(), trainer);
    when(storage.getTrainers()).thenReturn(trainerMap);

    Trainer retrievedTrainer = trainerRepository.get(trainer.getId());

    assertEquals(trainer.getId(), retrievedTrainer.getId());
    assertEquals(trainer.getTrainingTypeId(), retrievedTrainer.getTrainingTypeId());
    assertEquals(trainer.getUser(), retrievedTrainer.getUser());
    assertEquals(trainer.getTrainings(), retrievedTrainer.getTrainings());
  }
  @Test
  public void testCreateTrainer() {
    User user = User.builder()
        .id(1L)
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .password("password")
        .isActive(true)
        .build();
    Trainer trainerToCreate = Trainer.builder()
        .trainingTypeId(2L)
        .user(user)
        .trainings(new ArrayList<>())
        .build();

    when(userRepository.create(any(User.class))).thenReturn(trainerToCreate.getUser());

    Trainer createdTrainer = trainerRepository.create(trainerToCreate);

    assertNotNull(createdTrainer);
    assertEquals(trainerToCreate.getTrainingTypeId(), createdTrainer.getTrainingTypeId());
  }


  @Test
  public void testUpdate() {
    Trainer existingTrainer = Trainer.builder()
        .id(1L)
        .trainingTypeId(2L)
        .user(User.builder().id(1L).build())
        .trainings(new ArrayList<>())
        .build();

    Map<Long, Trainer> trainersMap = new HashMap<>();
    trainersMap.put(existingTrainer.getId(), existingTrainer);

    when(storage.getTrainers()).thenReturn(trainersMap);

    Trainer updatedTrainer = Trainer.builder()
        .id(existingTrainer.getId())
        .trainingTypeId(3L)
        .user(User.builder().id(1L).build())
        .trainings(new ArrayList<>())
        .build();

    Trainer result = trainerRepository.update(updatedTrainer);

    assertEquals(updatedTrainer.getId(), result.getId());
    assertEquals(updatedTrainer.getTrainingTypeId(), result.getTrainingTypeId());
    assertEquals(updatedTrainer.getUser(), result.getUser());
    assertEquals(updatedTrainer.getTrainings(), result.getTrainings());
  }

  @Test
  public void testGetAll() {
    Map<Long, Trainer> trainers = new HashMap<>();
    trainers.put(1L, new Trainer());
    trainers.put(2L, new Trainer());
    when(storage.getTrainers()).thenReturn(trainers);

    Collection<Trainer> allTrainers = trainerRepository.getAll();

    assertEquals(2, allTrainers.size()); // Проверка размера коллекции
  }

  @Test
  public void testCreateTrainings() throws Exception {
    Trainer trainer = Trainer.builder()
        .id(1L)
        .trainingTypeId(2L)
        .user(User.builder().id(1L).build())
        .trainings(new ArrayList<>())
        .build();

    Training training1 = Training.builder()
        .id(1L)
        .trainerId(trainer.getId())
        .trainerId(2L)
        .trainingName("Training 1")
        .trainingTypeId(3L)
        .trainingDate(LocalDate.now())
        .trainingDuration(60L)
        .build();

    Training training2 = Training.builder()
        .id(2L)
        .trainerId(trainer.getId())
        .trainerId(3L)
        .trainingName("Training 2")
        .trainingTypeId(4L)
        .trainingDate(LocalDate.now())
        .trainingDuration(45L)
        .build();

    trainer.getTrainings().add(training1);
    trainer.getTrainings().add(training2);

    Method createTrainingsMethod = TrainerRepository.class.getDeclaredMethod("createTrainings", Trainer.class);
    createTrainingsMethod.setAccessible(true);

    TrainingRepository trainingRepository = mock(TrainingRepository.class);

    UserRepository userRepository = mock(UserRepository.class);
    when(userRepository.create(any())).thenReturn(User.builder().id(1L).build());

    TrainerRepository trainerRepository = new TrainerRepository(storage, trainingRepository, userRepository);

    createTrainingsMethod.invoke(trainerRepository, trainer);

    verify(trainingRepository).create(training1);
    verify(trainingRepository).create(training2);
  }

  @Test
  public void testValidateEntity() throws Exception {
    TrainerRepository trainerRepository = new TrainerRepository(storage, trainingRepository, userRepository);

    Method validateEntityMethod = TrainerRepository.class.getDeclaredMethod("validateEntity", Trainer.class);
    validateEntityMethod.setAccessible(true);

    Trainer trainer = Trainer.builder()
        .id(1L)
        .trainingTypeId(2L)
        .trainings(new ArrayList<>())
        .build();

    try {
      validateEntityMethod.invoke(trainerRepository, trainer);
      fail("Expected IllegalArgumentException was not thrown");
    } catch (InvocationTargetException e) {
      assertTrue(e.getCause() instanceof IllegalArgumentException);
      assertEquals("Trainer must have a user", e.getCause().getMessage());
    }
  }

  @Test
  public void testSetUserIfNull() throws Exception {
    TrainerRepository trainerRepository = new TrainerRepository(storage, trainingRepository, userRepository);

    Method setUserIfNullMethod = TrainerRepository.class.getDeclaredMethod("setUserIfNull", Trainer.class);
    setUserIfNullMethod.setAccessible(true);

    Trainer trainer = Trainer.builder()
        .id(1L)
        .trainingTypeId(2L)
        .trainings(new ArrayList<>())
        .build();

    UserRepository mockedUserRepository = mock(UserRepository.class);

    Field userRepositoryField = TrainerRepository.class.getDeclaredField("userRepository");
    userRepositoryField.setAccessible(true);
    userRepositoryField.set(trainerRepository, mockedUserRepository);

    setUserIfNullMethod.invoke(trainerRepository, trainer);

    verify(mockedUserRepository).create(null);
  }
}