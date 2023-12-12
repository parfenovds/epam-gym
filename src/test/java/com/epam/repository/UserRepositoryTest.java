package com.epam.repository;


import com.epam.commonDB.Storage;
import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.User;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

class UserRepositoryTest {

  @Mock
  private Storage storage;

  @InjectMocks
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testGetExistingUser() {
    Long userId = 1L;
    User testUser = new User();
    testUser.setId(userId);

    when(storage.getUsers()).thenReturn(Collections.singletonMap(userId, testUser));

    User retrievedUser = userRepository.get(userId);

    assertNotNull(retrievedUser);
    assertEquals(userId, retrievedUser.getId());
    verify(storage).getUsers();
  }

  @Test
  void testGetNonExistingUser() {
    Long userId = 1L;

    when(storage.getUsers()).thenReturn(new HashMap<>());

    User retrievedUser = userRepository.get(userId);

    assertNull(retrievedUser);
    verify(storage).getUsers();
  }

  @Test
  void testCreateUser() {
    User testUser = new User();
    testUser.setFirstName("John");
    testUser.setLastName("Doe");

    Map<Long, User> usersMap = new HashMap<>();
    when(storage.getUsers()).thenReturn(usersMap);
    when(storage.getNextUserId()).thenReturn(new AtomicLong(1L));

    User createdUser = userRepository.create(testUser);

    assertNotNull(createdUser);
    assertEquals(1, usersMap.size());
    assertTrue(usersMap.containsKey(createdUser.getId()));
    verify(storage).getUsers();
    verify(storage).getNextUserId();
  }

  @Test
  void testCheckIfUserHasFirstnameAndLastname_ValidUser() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    User user = new User();
    user.setFirstName("John");
    user.setLastName("Doe");

    Method method = UserRepository.class.getDeclaredMethod("checkIfUserHasFirstnameAndLastname", User.class);
    method.setAccessible(true);
    assertDoesNotThrow(() -> method.invoke(userRepository, user));
  }

  @Test
  void testCheckIfUserHasFirstnameAndLastname_NullFirstName() throws NoSuchMethodException {
    User user = new User();
    user.setLastName("Doe");

    Method method = UserRepository.class.getDeclaredMethod("checkIfUserHasFirstnameAndLastname", User.class);
    method.setAccessible(true);

    try {
      method.invoke(userRepository, user);
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

    Method method = UserRepository.class.getDeclaredMethod("setUsernameForUser", User.class);
    method.setAccessible(true);

    when(storage.getUsers()).thenReturn(new HashMap<>());

    method.invoke(userRepository, user);

    assertEquals("John.Doe", user.getUsername());
  }

  @Test
  void testGetCurrentSerialNumberForDuplicateUsername_WithDuplicates() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    User user = new User();
    user.setFirstName("John");
    user.setLastName("Doe");

    Map<Long, Trainee> trainees = new HashMap<>();
    Trainee trainee1 = new Trainee();
    User existingUser1 = new User();
    existingUser1.setUsername("John.Doe");
    existingUser1.setFirstName("John");
    existingUser1.setLastName("Doe");
    trainee1.setUser(existingUser1);
    trainees.put(1L, trainee1);

    Trainee trainee2 = new Trainee();
    User existingUser2 = new User();
    existingUser2.setUsername("John.Doe2");
    existingUser2.setFirstName("John");
    existingUser2.setLastName("Doe");
    trainee2.setUser(existingUser2);
    trainees.put(2L, trainee2);

    when(storage.getTrainees()).thenReturn(trainees);

    Map<Long, Trainer> trainers = new HashMap<>();
    Trainer trainer1 = new Trainer();
    User existingUser3 = new User();
    existingUser3.setUsername("John.Doe3");
    existingUser3.setFirstName("John");
    existingUser3.setLastName("Doe");
    trainer1.setUser(existingUser3);
    trainers.put(1L, trainer1);

    Trainer trainer2 = new Trainer();
    User existingUser4 = new User();
    existingUser4.setUsername("John.Doe4");
    existingUser4.setFirstName("John");
    existingUser4.setLastName("Doe");
    trainer2.setUser(existingUser4);
    trainers.put(2L, trainer2);

    when(storage.getTrainers()).thenReturn(trainers);

    Method method = UserRepository.class.getDeclaredMethod("getCurrentSerialNumberForDuplicateUsername", User.class);
    method.setAccessible(true);

    Optional<Integer> serialNumber = (Optional<Integer>) method.invoke(userRepository, user);
    assertEquals(4, serialNumber.orElse(-1));
  }
}