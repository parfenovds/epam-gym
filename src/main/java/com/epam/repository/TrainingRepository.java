package com.epam.repository;

import com.epam.commonDB.Storage;
import com.epam.entity.Training;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
@Repository
@AllArgsConstructor
public class TrainingRepository implements BaseRepository<Training> {

  private final Storage storage;

  @Override
  public Training get(Long id) {
    return storage.getTrainings().get(id);
  }

  @Override
  public Training create(Training entity) {
    entity.setId(storage.getNextTrainingId().getAndIncrement());
    storage.getTrainings().put(entity.getId(), entity);
    return entity;
  }
  public List<Training> getTrainingsByTrainee(Long id) {//TODO: generalize it
    return storage.getTrainings().values().stream()
        .filter(training -> training.getTraineeId().equals(id))
        .toList();
  }
  public List<Training> getTrainingsByTrainer(Long id) {
    return storage.getTrainings().values().stream()
        .filter(training -> training.getTrainerId().equals(id))
        .toList();
  }
}
