package com.epam.service;

import com.epam.entity.Trainer;
import com.epam.entity.User;
import com.epam.repository.TrainerRepository;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class TrainerService implements BaseService<Trainer>{
  private final TrainerRepository trainerRepository;
  private final UserService userService;
  public Collection<Trainer> findAll() {
    Collection<Trainer> trainers = trainerRepository.getAll();
    log.info("Trainers acquired from service: " + trainers);
    return trainers;
  }
  @Override
  public Trainer findById(Long id) {
    Trainer trainer = trainerRepository.get(id);
    log.info("Trainer acquired from service: " + trainer);
    return trainer;
  }

  @Override
  public Trainer save(Trainer entity) {
    User user = userService.save(entity.getUser());
    entity.setUser(user);
    Trainer trainer = trainerRepository.create(entity);
    log.info("Trainer saved in service: " + trainer);
    return trainer;
  }

  public Trainer update(Trainer entity) {
    Trainer update = trainerRepository.update(entity);
    log.info("Trainer updated in service: " + update);
    return update;
  }
}
