package com.epam.service;

import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.Training;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FacadeService {

  private final TraineeService traineeService;
  private final TrainerService trainerService;
  private final TrainingService trainingService;

  public Trainee findTraineeById(Long id) {
    return traineeService.findById(id);
  }

  public Trainee saveTrainee(Trainee trainee) {
    return traineeService.save(trainee);
  }

  public Trainee updateTrainee(Trainee trainee) {
    return traineeService.update(trainee);
  }

  public void deleteTrainee(Long id) {
    traineeService.delete(id);
  }

  public Trainer findTrainerById(Long id) {
    return trainerService.findById(id);
  }

  public Trainer saveTrainer(Trainer trainer) {
    return trainerService.save(trainer);
  }

  public Trainer updateTrainer(Trainer trainer) {
    return trainerService.update(trainer);
  }

  public Training findTrainingById(Long id) {
    return trainingService.findById(id);
  }

  public Training saveTraining(Training training) {
    return trainingService.save(training);
  }
}