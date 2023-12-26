package com.epam.service;

import com.epam.entity.User;
import com.epam.repository.UserRepository;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private Validator validator;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void save_ValidUser_CreatesUser() {
    User user = new User();
    // Set valid fields for the user

    when(validator.validate(user)).thenReturn(Collections.emptySet());
    when(userRepository.create(user)).thenReturn(user);

    User savedUser = userService.save(user);

    assertNotNull(savedUser);
    assertEquals(user, savedUser);
    verify(userRepository, times(1)).create(user);
  }

  @Test
  void save_InvalidUser_ThrowsException() {
    User user = new User();
    // Set invalid fields for the user

    Set<ConstraintViolation<User>> violations = Collections.singleton(mock(ConstraintViolation.class));
    when(validator.validate(user)).thenReturn(violations);

    assertThrows(IllegalArgumentException.class, () -> userService.save(user));
    verifyNoInteractions(userRepository);
  }

  @Test
  void findById_ExistingId_ReturnsUser() {
    Long userId = 1L;
    User user = new User();
    user.setId(userId);

    when(userRepository.get(userId)).thenReturn(Optional.of(user));

    User foundUser = userService.findById(userId);

    assertNotNull(foundUser);
    assertEquals(userId, foundUser.getId());
    verify(userRepository, times(1)).get(userId);
  }

  @Test
  void findById_NonExistingId_ThrowsException() {
    Long userId = 1L;

    when(userRepository.get(userId)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> userService.findById(userId));
    verify(userRepository, times(1)).get(userId);
  }

  @Test
  void delete_ExistingId_DeletesUser() {
    Long userId = 1L;

    userService.delete(userId);

    verify(userRepository, times(1)).delete(userId);
  }

  @Test
  void update_ValidUser_UpdatesUser() {
    User user = new User();
    // Set valid fields for the user

    when(validator.validate(user)).thenReturn(Collections.emptySet());
    when(userRepository.update(user)).thenReturn(user);

    User updatedUser = userService.update(user);

    assertNotNull(updatedUser);
    assertEquals(user, updatedUser);
    verify(userRepository, times(1)).update(user);
  }

  @Test
  void update_InvalidUser_ThrowsException() {
    User user = new User();
    // Set invalid fields for the user

    Set<ConstraintViolation<User>> violations = Collections.singleton(mock(ConstraintViolation.class));
    when(validator.validate(user)).thenReturn(violations);

    assertThrows(IllegalArgumentException.class, () -> userService.update(user));
    verifyNoInteractions(userRepository);
  }

  @Test
  void findAll_UsersExist_ReturnsUsers() {
    User user1 = new User();
    User user2 = new User();
    // Set valid fields for the users

    when(userRepository.getAll()).thenReturn(List.of(user1, user2));

    Collection<User> foundUsers = userService.findAll();

    assertNotNull(foundUsers);
    assertEquals(2, foundUsers.size());
    assertTrue(foundUsers.contains(user1));
    assertTrue(foundUsers.contains(user2));
    verify(userRepository, times(1)).getAll();
  }

  @Test
  void usernameToPasswordMatchingCheck_ValidUsernamePassword_ReturnsTrue() {
    String username = "testUsername";
    String password = "testPassword";

    when(userRepository.usernameToPasswordMatchingCheck(username, password)).thenReturn(true);

    boolean result = userService.usernameToPasswordMatchingCheck(username, password);

    assertTrue(result);
    verify(userRepository, times(1)).usernameToPasswordMatchingCheck(username, password);
  }

  @Test
  void usernameToPasswordMatchingCheck_InvalidUsernamePassword_ReturnsFalse() {
    String username = "testUsername";
    String password = "testPassword";

    when(userRepository.usernameToPasswordMatchingCheck(username, password)).thenReturn(false);

    boolean result = userService.usernameToPasswordMatchingCheck(username, password);

    assertFalse(result);
    verify(userRepository, times(1)).usernameToPasswordMatchingCheck(username, password);
  }
}