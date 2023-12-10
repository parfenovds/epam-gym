package com.epam.repository;

import com.epam.commonDB.Storage;
import com.epam.entity.Trainee;
import com.epam.entity.Training;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Log4j2
public class TraineeRepository implements BaseRepository<Trainee> {

  private final Storage storage;
  private final TrainingRepository trainingRepository;
  private final UserRepository userRepository;

  public Collection<Trainee> getAll() {
    Collection<Trainee> values = storage.getTrainees().keySet().stream().map(this::get).toList();
    log.info("Trainees acquired from repository: {}", values);
    return values;
  }

  @Override
  public Trainee get(Long id) {
    Trainee trainee = storage.getTrainees().get(id);
    log.info("Trainee acquired from repository: {}", trainee);
    trainee.setTrainings(trainingRepository.getTrainingsByField(id, Training::getTraineeId));
    log.info("Trainee trainings acquired from repository: {}", trainee.getTrainings());
    return trainee;
  }

  @Override
  public Trainee create(Trainee entity) {
    validateEntity(entity);
    setUserIfNull(entity);
    entity.setId(storage.getNextTraineeId().getAndIncrement());
    createTrainings(entity);
    entity.setTrainings(trainingRepository.getTrainingsByField(entity.getId(), Training::getTraineeId));
    Trainee trainee = storage.getTrainees().put(entity.getId(), entity);
    log.info("Trainee created in repository: {}", trainee);
    return trainee;
  }

  private void createTrainings(Trainee entity) {
    entity.getTrainings().forEach(training -> {
      training.setTraineeId(entity.getId());
      trainingRepository.create(training);
    });
  }

  private void validateEntity(Trainee entity) {
    if (entity.getUser() == null) {
      log.error("Trainee create failure: Trainee must have a user");
      throw new IllegalArgumentException("Trainee must have a user");
    }
  }

  private void setUserIfNull(Trainee entity) {
    if (entity.getUser().getId() == null) {
      entity.setUser(userRepository.create(entity.getUser()));
    }
  }

  public Trainee update(Trainee entity) {
    Trainee trainee = storage.getTrainees().put(entity.getId(), entity);
    log.info("Trainee updated in repository: {}", trainee);
    trainingRepository.updateTrainings(trainee.getId(), Training::getTraineeId, trainee.getTrainings());
    return trainee;
  }

  public void delete(Long id) {
    storage.getTrainees().remove(id);
    log.info("Trainee deleted in repository: {}", id);
    trainingRepository.getTrainingsByField(id, Training::getTraineeId).forEach(t -> trainingRepository.delete(t.getId()));
    log.info("Trainee trainings deleted in repository");
  }
}
