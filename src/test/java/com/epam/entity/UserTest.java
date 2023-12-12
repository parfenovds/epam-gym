package com.epam.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class UserTest {

  @Test
  public void testUserId() {
    Long expectedId = 1L;
    User user = new User();
    user.setId(expectedId);
    Long actualId = user.getId();
    assertEquals(expectedId, actualId, "User ID should match");
  }

  @Test
  public void testUserFirstName() {
    String expectedFirstName = "John";
    User user = new User();
    user.setFirstName(expectedFirstName);
    String actualFirstName = user.getFirstName();
    assertEquals(expectedFirstName, actualFirstName, "User first name should match");
  }

  @Test
  public void testUserLastName() {
    String expectedLastName = "Doe";
    User user = new User();
    user.setLastName(expectedLastName);
    String actualLastName = user.getLastName();
    assertEquals(expectedLastName, actualLastName, "User last name should match");
  }

  @Test
  public void testUserUsername() {
    String expectedUsername = "johndoe";
    User user = new User();
    user.setUsername(expectedUsername);
    String actualUsername = user.getUsername();
    assertEquals(expectedUsername, actualUsername, "User username should match");
  }

  @Test
  public void testUserPassword() {
    String expectedPassword = "securePassword";
    User user = new User();
    user.setPassword(expectedPassword);
    String actualPassword = user.getPassword();
    assertEquals(expectedPassword, actualPassword, "User password should match");
  }

  @Test
  public void testUserIsActive() {
    Boolean expectedIsActive = true;
    User user = new User();
    user.setIsActive(expectedIsActive);
    Boolean actualIsActive = user.getIsActive();
    assertEquals(expectedIsActive, actualIsActive, "User isActive status should match");
  }
}