package com.epam.repository;

import com.epam.commonDB.Storage;
import com.epam.entity.Trainer;
import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class TrainerRepository implements BaseRepository<Trainer> {

  private final Storage storage;
  private final TrainingRepository trainingRepository;

  public Collection<Trainer> getAll() {
    return storage.getTrainers().values();
  }

  @Override
  public Trainer get(Long id) {
    Trainer trainer = storage.getTrainers().get(id);
    trainer.setTrainings(trainingRepository.getTrainingsByTrainer(id));
    return trainer;
  }

  @Override
  public Trainer create(Trainer entity) {
    entity.setId(storage.getNextTrainerId().getAndIncrement());
    return update(entity);
  }

  public Trainer update(Trainer entity) {
    return storage.getTrainers().put(entity.getId(), entity);
  }
}
