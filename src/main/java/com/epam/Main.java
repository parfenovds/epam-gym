package com.epam;

import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.User;
import com.epam.service.FacadeService;
import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext("com.epam");
    FacadeService facadeService = context.getBean("facadeService", FacadeService.class);
//    Trainee traineeById = facadeService.findTraineeById(1L);

    User user = User.builder()
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .password("passwordWOW")
        .isActive(true)
        .build();
    Trainee trainee1ToCreate = Trainee.builder()
        .dateOfBirth(LocalDate.of(1995, 3, 20))
        .address("456 Oak St")
        .user(user)
        .trainings(new ArrayList<>())
        .build();
    Trainee trainee1 = facadeService.saveTrainee(trainee1ToCreate);
    System.out.println(trainee1);

    User user1 = User.builder()
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .password("passwordWOW")
        .isActive(true)
        .build();
    Trainer trainer11ToCreate = Trainer.builder()
        .trainingTypeId(2L)
        .user(user1)
        .trainings(new ArrayList<>())
        .build();
    Trainer trainer11 = facadeService.saveTrainer(trainer11ToCreate);
    System.out.println(trainer11);

    User user3 = User.builder()
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .password("passwordWOW")
        .isActive(true)
        .build();
    Trainer trainer12ToCreate = Trainer.builder()
        .trainingTypeId(2L)
        .user(user3)
        .trainings(new ArrayList<>())
        .build();
    Trainer trainer12 = facadeService.saveTrainer(trainer12ToCreate);
    System.out.println(trainer12);

//    Trainer trainer1 = facadeService.updateTrainer(trainerToUpdate);
//    System.out.println(trainer1);


  }
}