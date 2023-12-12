package com.epam.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

public class TraineeTest {

  @Test
  public void testTraineeId() {
    Long expectedId = 1L;
    Trainee trainee = new Trainee();
    trainee.setId(expectedId);
    Long actualId = trainee.getId();
    assertEquals(expectedId, actualId, "Trainee ID should match");
  }

  @Test
  public void testTraineeDateOfBirth() {
    LocalDate expectedDateOfBirth = LocalDate.of(1995, 5, 15);
    Trainee trainee = new Trainee();
    trainee.setDateOfBirth(expectedDateOfBirth);
    LocalDate actualDateOfBirth = trainee.getDateOfBirth();
    assertEquals(expectedDateOfBirth, actualDateOfBirth, "Trainee date of birth should match");
  }

  @Test
  public void testTraineeAddress() {
    String expectedAddress = "123 Main St";
    Trainee trainee = new Trainee();
    trainee.setAddress(expectedAddress);
    String actualAddress = trainee.getAddress();
    assertEquals(expectedAddress, actualAddress, "Trainee address should match");
  }

  @Test
  public void testTraineeUser() {
    User mockUser = mock(User.class);
    Trainee trainee = new Trainee();
    trainee.setUser(mockUser);
    User actualUser = trainee.getUser();
    assertEquals(mockUser, actualUser, "Trainee user should match");
  }

  @Test
  public void testTraineeTrainings() {
    Collection<Training> expectedTrainings = new ArrayList<>();
    expectedTrainings.add(new Training());
    expectedTrainings.add(new Training());

    Trainee trainee = new Trainee();
    trainee.setTrainings(expectedTrainings);

    Collection<Training> actualTrainings = trainee.getTrainings();
    assertEquals(expectedTrainings.size(), actualTrainings.size(), "Number of trainings should match");
  }
}