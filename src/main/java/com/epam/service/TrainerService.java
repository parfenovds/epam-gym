package com.epam.service;

import com.epam.entity.Trainer;
import com.epam.repository.TrainerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TrainerService implements BaseService<Trainer>{
  private final TrainerRepository trainerRepository;
  @Override
  public Trainer findById(Long id) {
    return trainerRepository.get(id);
  }

  @Override
  public Trainer save(Trainer entity) {
    return trainerRepository.create(entity);
  }

  public Trainer update(Trainer entity) {
    return trainerRepository.update(entity);
  }
}
