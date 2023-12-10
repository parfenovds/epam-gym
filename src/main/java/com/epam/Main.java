package com.epam;

import com.epam.commonDB.Storage;
import com.epam.entity.Trainee;
import com.epam.repository.TraineeRepository;
import com.epam.service.TraineeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext("com.epam");
    Storage storage = context.getBean("storage", Storage.class);
    System.out.println(storage);
    TraineeRepository traineeRepository = context.getBean("traineeRepository", TraineeRepository.class);
    Trainee trainee = traineeRepository.get(1L);
    TraineeService traineeService = context.getBean("traineeService", TraineeService.class);
    Trainee byId = traineeService.findById(1L);
    System.out.println(trainee);
    System.out.println(byId);
  }
}