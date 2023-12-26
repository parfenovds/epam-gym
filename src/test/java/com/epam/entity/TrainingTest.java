package com.epam.entity;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TrainingTest {

  @Test
  public void testToString() {
    // Подготовка данных для теста
    Trainee trainee = new Trainee();
    trainee.setId(1L);

    Trainer trainer = new Trainer();
    trainer.setId(2L);

    TrainingType trainingType = new TrainingType();
    trainingType.setId(3L);

    Training training = Training.builder()
        .id(100L)
        .trainee(trainee)
        .trainer(trainer)
        .trainingName("TrainingName")
        .trainingType(trainingType)
        .trainingDate(LocalDate.of(2023, 12, 31))
        .trainingDuration(60L)
        .build();

    // Ожидаемая строка
    String expectedString = "Training{id=100, traineeId=1, trainerId=2, trainingName='TrainingName', " +
        "trainingType=TrainingType(id=3, trainingTypeName=null), trainingDate=2023-12-31, trainingDuration=60}";

    // Проверка на соответствие ожидаемой строке
    assertEquals(expectedString, training.toString());
  }
}