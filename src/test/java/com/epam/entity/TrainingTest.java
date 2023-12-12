package com.epam.entity;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TrainingTest {

  @Test
  public void testTrainingId() {
    Long expectedId = 1L;
    Training training = new Training();
    training.setId(expectedId);
    Long actualId = training.getId();
    assertEquals(expectedId, actualId, "Training ID should match");
  }

  @Test
  public void testTrainingTraineeId() {
    Long expectedTraineeId = 2L;
    Training training = new Training();
    training.setTraineeId(expectedTraineeId);
    Long actualTraineeId = training.getTraineeId();
    assertEquals(expectedTraineeId, actualTraineeId, "Training trainee ID should match");
  }

  @Test
  public void testTrainingTrainerId() {
    Long expectedTrainerId = 3L;
    Training training = new Training();
    training.setTrainerId(expectedTrainerId);
    Long actualTrainerId = training.getTrainerId();
    assertEquals(expectedTrainerId, actualTrainerId, "Training trainer ID should match");
  }

  @Test
  public void testTrainingName() {
    String expectedTrainingName = "Java Basics";
    Training training = new Training();
    training.setTrainingName(expectedTrainingName);
    String actualTrainingName = training.getTrainingName();
    assertEquals(expectedTrainingName, actualTrainingName, "Training name should match");
  }

  @Test
  public void testTrainingTypeId() {
    Long expectedTrainingTypeId = 4L;
    Training training = new Training();
    training.setTrainingTypeId(expectedTrainingTypeId);
    Long actualTrainingTypeId = training.getTrainingTypeId();
    assertEquals(expectedTrainingTypeId, actualTrainingTypeId, "Training type ID should match");
  }

  @Test
  public void testTrainingDate() {
    LocalDate expectedTrainingDate = LocalDate.of(2023, 12, 1);
    Training training = new Training();
    training.setTrainingDate(expectedTrainingDate);
    LocalDate actualTrainingDate = training.getTrainingDate();
    assertEquals(expectedTrainingDate, actualTrainingDate, "Training date should match");
  }

  @Test
  public void testTrainingDuration() {
    Long expectedTrainingDuration = 60L;
    Training training = new Training();
    training.setTrainingDuration(expectedTrainingDuration);
    Long actualTrainingDuration = training.getTrainingDuration();
    assertEquals(expectedTrainingDuration, actualTrainingDuration, "Training duration should match");
  }
}