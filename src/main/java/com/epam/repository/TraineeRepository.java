package com.epam.repository;

import com.epam.commonDB.Storage;
import com.epam.entity.Trainee;
import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class TraineeRepository implements BaseRepository<Trainee> {

  private final Storage storage;
  private final TrainingRepository trainingRepository;

  public Collection<Trainee> getAll() {
    return storage.getTrainees().values();
  }

  @Override
  public Trainee get(Long id) {
    Trainee trainee = storage.getTrainees().get(id);
    trainee.setTrainings(trainingRepository.getTrainingsByTrainee(id));
    return trainee;
  }

  @Override
  public Trainee create(Trainee entity) {
    entity.setId(storage.getNextTraineeId().getAndIncrement());
    return update(entity);
  }

  public Trainee update(Trainee entity) {
    return storage.getTrainees().put(entity.getId(), entity);
  }

  public void delete(Long id) {
    storage.getTrainees().remove(id);
  }
}
