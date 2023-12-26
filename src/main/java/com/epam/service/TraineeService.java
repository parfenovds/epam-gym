package com.epam.service;

import com.epam.entity.Trainee;
import com.epam.repository.TraineeRepository;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

@Service
public class TraineeService extends HasUserServiceImpl<Trainee> {
  public TraineeService(TraineeRepository traineeRepository, Validator validator) {
    super(traineeRepository, Trainee.class, validator);
  }
}
