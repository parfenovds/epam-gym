package com.epam.service;

import com.epam.entity.Trainer;
import com.epam.repository.EntityWithUserRepository;
import com.epam.repository.TrainerRepository;
import jakarta.validation.Validator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainerService extends HasUserServiceImpl<Trainer>{

  private final TrainerRepository baseRepository;
  public TrainerService(EntityWithUserRepository<Trainer> baseRepository, Validator validator) {
    super(baseRepository, Trainer.class, validator);
    this.baseRepository = (TrainerRepository) baseRepository;
  }

  @Transactional(readOnly = true)
  public List<Trainer> listOfNotAssignedToTrainee(Long traineeId) {
    return baseRepository.listOfNotAssignedToTrainee(traineeId);
  }
}
