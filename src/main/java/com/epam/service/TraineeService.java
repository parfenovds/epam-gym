package com.epam.service;

import com.epam.entity.Trainee;
import com.epam.repository.TraineeRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TraineeService implements BaseService<Trainee> {

  private final TraineeRepository traineeRepository;
  @Override
  public Trainee findById(Long id) {
    return traineeRepository.get(id);
  }

  @Override
  public Trainee save(Trainee entity) {
    return traineeRepository.create(entity);
  }
  public Trainee update(Trainee entity) {
    return traineeRepository.update(entity);
  }

  public void delete(Long id) {
    traineeRepository.delete(id);
  }

}
