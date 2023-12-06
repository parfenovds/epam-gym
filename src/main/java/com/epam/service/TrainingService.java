package com.epam.service;

import com.epam.entity.Training;
import com.epam.repository.TrainingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TrainingService implements BaseService<Training>{
  private final TrainingRepository trainingRepository;
  @Override
  public Training findById(Long id) {
    return trainingRepository.get(id);
  }

  @Override
  public Training save(Training entity) {
    return trainingRepository.create(entity);
  }
}
