package com.epam;

import com.epam.entity.Trainee;
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
    Trainee traineeById = facadeService.findTraineeById(1L);

    User user = User.builder()
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .password("passwordWOW")
        .isActive(true)
        .build();
    Trainee traineeToCreate = Trainee.builder()
        .dateOfBirth(LocalDate.of(1995, 3, 20))
        .address("456 Oak St")
        .user(user)
        .trainings(new ArrayList<>())
        .build();
    Trainee trainee = facadeService.saveTrainee(traineeToCreate);
    System.out.println(trainee);

    System.out.println(traineeById);

    User user2 = User.builder()
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .password("passwordWOW")
        .isActive(true)
        .build();
    Trainee traineeToCreate2 = Trainee.builder()
        .dateOfBirth(LocalDate.of(1995, 3, 20))
        .address("456 Oak St")
        .user(user2)
        .trainings(new ArrayList<>())
        .build();
//    Storage storage = context.getBean("storage", Storage.class);
//    System.out.println(storage);
//    TraineeRepository traineeRepository = context.getBean("traineeRepository", TraineeRepository.class);
//    Trainee trainee = traineeRepository.get(1L);
//    TraineeService traineeService = context.getBean("traineeService", TraineeService.class);
//    Trainee byId = traineeService.findById(1L);
//    System.out.println(trainee);
//    System.out.println(byId);
//    User user = User.builder()
//        .firstName("John")
//        .lastName("Doe")
//        .username("johndoe")
//        .password("password")
//        .isActive(true)
//        .build();
//    // Создаем объект Trainee для создания
//    Trainee traineeToCreate = Trainee.builder()
//        .dateOfBirth(LocalDate.of(1995, 3, 20))
//        .address("456 Oak St")
//        .user(user)
//        .trainings(new ArrayList<>())
//        .build();
//    Trainee save = traineeService.save(traineeToCreate);
//    System.out.println(save);
  }
}