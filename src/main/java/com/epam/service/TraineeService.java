package com.epam.service;

import com.epam.entity.Trainee;
import com.epam.repository.TraineeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class TraineeService implements BaseService<Trainee> {

  private final TraineeRepository traineeRepository;
  @Override
  public Trainee findById(Long id) {
    Trainee trainee = traineeRepository.get(id);
    log.info("Trainee acquired from service: " + trainee);
    return trainee;
  }

  @Override
  public Trainee save(Trainee entity) {
    Trainee trainee = traineeRepository.create(entity);
    log.info("Trainee saved in service: " + trainee);
    return trainee;
  }
  public Trainee update(Trainee entity) {
    Trainee update = traineeRepository.update(entity);
    log.info("Trainee updated in service: " + update);
    return update;
  }

  public void delete(Long id) {
    traineeRepository.delete(id);
    log.info("Trainee deleted in service: " + id);
  }
}
