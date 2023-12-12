package com.epam.entity;

import java.util.ArrayList;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

public class TrainerTest {

  @Test
  public void testTrainerId() {
    Long expectedId = 1L;
    Trainer trainer = new Trainer();
    trainer.setId(expectedId);
    Long actualId = trainer.getId();
    assertEquals(expectedId, actualId, "Trainer ID should match");
  }

  @Test
  public void testTrainerTrainingTypeId() {
    Long expectedTrainingTypeId = 2L;
    Trainer trainer = new Trainer();
    trainer.setTrainingTypeId(expectedTrainingTypeId);
    Long actualTrainingTypeId = trainer.getTrainingTypeId();
    assertEquals(expectedTrainingTypeId, actualTrainingTypeId, "Trainer training type ID should match");
  }

  @Test
  public void testTrainerUser() {
    User mockUser = mock(User.class);
    Trainer trainer = new Trainer();
    trainer.setUser(mockUser);
    User actualUser = trainer.getUser();
    assertEquals(mockUser, actualUser, "Trainer user should match");
  }

  @Test
  public void testTrainerTrainings() {
    Collection<Training> expectedTrainings = new ArrayList<>();
    expectedTrainings.add(new Training());
    expectedTrainings.add(new Training());

    Trainer trainer = new Trainer();
    trainer.setTrainings(expectedTrainings);

    Collection<Training> actualTrainings = trainer.getTrainings();
    assertEquals(expectedTrainings.size(), actualTrainings.size(), "Number of trainings should match");
  }
}
