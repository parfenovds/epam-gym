package com.epam.service;

import com.epam.commonDB.Storage;
import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.User;
import com.epam.repository.UserRepository;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private TrainerService trainerService;

  @Mock
  private TraineeService traineeService;

  @Mock
  private Storage storage;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSave() {
    User user = new User();
    user.setFirstName("John");
    user.setLastName("Doe");

    when(traineeService.findAll()).thenReturn(List.of());
    when(trainerService.findAll()).thenReturn(List.of());

    userService.save(user);

    verify(userRepository).create(user);

    assertNotNull(user.getUsername());
    assertTrue(user.getPassword().length() == 10);
  }

  @Test
  void testCheckIfUserHasFirstnameAndLastname_ValidUser() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    User user = new User();
    user.setFirstName("John");
    user.setLastName("Doe");

    Method method = UserService.class.getDeclaredMethod("checkIfUserHasFirstnameAndLastname", User.class);
    method.setAccessible(true);
    assertDoesNotThrow(() -> method.invoke(userService, user));
  }

  @Test
  void testCheckIfUserHasFirstnameAndLastname_NullFirstName() throws NoSuchMethodException {
    User user = new User();
    user.setLastName("Doe");

    Method method = UserService.class.getDeclaredMethod("checkIfUserHasFirstnameAndLastname", User.class);
    method.setAccessible(true);

    try {
      method.invoke(userService, user);
      fail("Expected an IllegalArgumentException to be thrown");
    } catch (InvocationTargetException e) {
      Throwable actualException = e.getTargetException();
      assertEquals("User must have first name and last name", actualException.getMessage());
      assertTrue(actualException instanceof IllegalArgumentException);
    } catch (IllegalAccessException e) {
      fail("Unexpected IllegalAccessException");
    }
  }

  @Test
  void testSetUsernameForUser_NoDuplicates() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    User user = new User();
    user.setFirstName("John");
    user.setLastName("Doe");

    Method method = UserService.class.getDeclaredMethod("setUsernameForUser", User.class);
    method.setAccessible(true);

    when(storage.getUsers()).thenReturn(new HashMap<>());

    method.invoke(userService, user);

    assertEquals("John.Doe", user.getUsername());
  }
  @Test
  void testGetCurrentSerialNumberForDuplicateUsername_WithDuplicates() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    User user = new User();
    user.setFirstName("John");
    user.setLastName("Doe");

    List<Trainee> trainees = new ArrayList<>();
    Trainee trainee1 = new Trainee();
    User existingUser1 = new User();
    existingUser1.setUsername("John.Doe");
    existingUser1.setFirstName("John");
    existingUser1.setLastName("Doe");
    trainee1.setUser(existingUser1);
    trainees.add(trainee1);

    Trainee trainee2 = new Trainee();
    User existingUser2 = new User();
    existingUser2.setUsername("John.Doe2");
    existingUser2.setFirstName("John");
    existingUser2.setLastName("Doe");
    trainee2.setUser(existingUser2);
    trainees.add(trainee2);

    when(traineeService.findAll()).thenReturn(trainees);

    List<Trainer> trainers = new ArrayList<>();
    Trainer trainer1 = new Trainer();
    User existingUser3 = new User();
    existingUser3.setUsername("John.Doe3");
    existingUser3.setFirstName("John");
    existingUser3.setLastName("Doe");
    trainer1.setUser(existingUser3);
    trainers.add(trainer1);

    Trainer trainer2 = new Trainer();
    User existingUser4 = new User();
    existingUser4.setUsername("John.Doe4");
    existingUser4.setFirstName("John");
    existingUser4.setLastName("Doe");
    trainer2.setUser(existingUser4);
    trainers.add(trainer2);

    when(trainerService.findAll()).thenReturn(trainers);

    Method method = UserService.class.getDeclaredMethod("getCurrentSerialNumberForDuplicateUsername", User.class);
    method.setAccessible(true);

    Optional<Integer> serialNumber = (Optional<Integer>) method.invoke(userService, user);
    assertEquals(4, serialNumber.orElse(-1));
  }
}