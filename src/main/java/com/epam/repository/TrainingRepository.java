package com.epam.repository;

import com.epam.commonDB.Storage;
import com.epam.entity.Training;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
@Repository
@AllArgsConstructor
@Log4j2
public class TrainingRepository implements BaseRepository<Training> {

  private final Storage storage;

  @Override
  public Training get(Long id) {
    Training training = storage.getTrainings().get(id);
    log.info("Training acquired from repository: {}", training);
    return training;
  }

  @Override
  public Training create(Training entity) {
    entity.setId(storage.getNextTrainingId().getAndIncrement());
    storage.getTrainings().put(entity.getId(), entity);
    log.info("Training created in repository: {}", entity);
    return entity;
  }

  public List<Training> getTrainingsByField(Long id, Function<Training, Long> fieldExtractor) {
    List<Training> list = storage.getTrainings().values().stream()
        .filter(training -> fieldExtractor.apply(training).equals(id))
        .toList();
    log.info("Trainings by field acquired from repository: {}", list);
    return list;
  }

  public void updateTrainings(Long id, Function<Training, Long> fieldExtractor, Collection<Training> currentTrainings) {
    List<Training> trainings = getTrainingsByField(id, fieldExtractor);
    Map<Long, Training> trainingsMap = trainings.stream()
        .collect(Collectors.toMap(Training::getId, Function.identity()));

    for (Training currentTraining : currentTrainings) {
      Training correspondingTraining = trainingsMap.get(currentTraining.getId());
      if (correspondingTraining != null) {
        update(currentTraining);
      } else {
        delete(currentTraining.getId());
      }
    }
    log.info("Trainings updated in repository");
  }

  public Training update(Training entity) {
    Training training = storage.getTrainings().put(entity.getId(), entity);
    log.info("Training updated in repository: {}", training);
    return training;
  }
  public void delete(Long id) {
    storage.getTrainings().remove(id);
    log.info("Training deleted in repository: {}", id);
  }
}
