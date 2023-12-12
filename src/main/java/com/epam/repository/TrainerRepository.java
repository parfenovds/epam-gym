package com.epam.repository;

import com.epam.commonDB.Storage;
import com.epam.entity.Trainer;
import com.epam.entity.Training;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Log4j2
public class TrainerRepository implements BaseRepository<Trainer> {

  private final Storage storage;
  private final TrainingRepository trainingRepository;
  private final UserRepository userRepository;

  public Collection<Trainer> getAll() {
    Collection<Trainer> values = storage.getTrainers().keySet().stream().map(this::get).toList();
    log.info("Trainers acquired from repository: {}", values);
    return values;
  }

  @Override
  public Trainer get(Long id) {
    Trainer trainer = storage.getTrainers().get(id);
    log.info("Trainer acquired from repository: {}", trainer);
    trainer.setTrainings(trainingRepository.getTrainingsByField(id, Training::getTraineeId));
    log.info("Trainer trainings acquired from repository: {}", trainer.getTrainings());
    return trainer;
  }

  @Override
  public Trainer create(Trainer entity) {
    validateEntity(entity);
    setUserIfNull(entity);
    entity.setId(storage.getNextTrainerId().getAndIncrement());
    createTrainings(entity);
    entity.setTrainings(trainingRepository.getTrainingsByField(entity.getId(), Training::getTrainerId));
    storage.getTrainers().put(entity.getId(), entity);
    log.info("Trainer created in repository: {}", entity);
    return entity;
  }

  private void createTrainings(Trainer entity) {
    entity.getTrainings().forEach(training -> {
      training.setTrainerId(entity.getId());
      trainingRepository.create(training);
    });
  }

  private void validateEntity(Trainer entity) {
    if (entity.getUser() == null) {
      log.error("Trainer create failure: Trainer must have a user");
      throw new IllegalArgumentException("Trainer must have a user");
    }
  }

  private void setUserIfNull(Trainer entity) {
    if (entity.getUser() == null) {
      entity.setUser(userRepository.create(entity.getUser()));
    }
  }

  public Trainer update(Trainer entity) {
    storage.getTrainers().put(entity.getId(), entity);
    log.info("Trainer updated in repository: {}", entity);
    trainingRepository.updateTrainings(entity.getId(), Training::getTrainerId, entity.getTrainings());
    return entity;
  }
}
