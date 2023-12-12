package com.epam.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TrainingTypeTest {

  @Test
  public void testTrainingTypeId() {
    Long expectedId = 1L;
    TrainingType trainingType = new TrainingType();
    trainingType.setId(expectedId);
    Long actualId = trainingType.getId();
    assertEquals(expectedId, actualId, "TrainingType ID should match");
  }

  @Test
  public void testTrainingTypeName() {
    String expectedTypeName = "Java Programming";
    TrainingType trainingType = new TrainingType();
    trainingType.setTrainingTypeName(expectedTypeName);
    String actualTypeName = trainingType.getTrainingTypeName();
    assertEquals(expectedTypeName, actualTypeName, "TrainingType name should match");
  }
}