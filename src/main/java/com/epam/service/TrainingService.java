package com.epam.service;

import com.epam.entity.Training;
import com.epam.repository.TrainingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class TrainingService implements BaseService<Training>{
  private final TrainingRepository trainingRepository;
  @Override
  public Training findById(Long id) {
    Training training = trainingRepository.get(id);
    log.info("Training acquired from service: " + training);
    return training;
  }

  @Override
  public Training save(Training entity) {
    Training training = trainingRepository.create(entity);
    log.info("Training saved in service: " + training);
    return training;
  }
}
