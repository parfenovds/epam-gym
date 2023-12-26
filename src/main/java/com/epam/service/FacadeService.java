package com.epam.service;

import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.Training;
import com.epam.entity.User;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FacadeService {

  private final TraineeService traineeService;
  private final TrainerService trainerService;
  private final TrainingService trainingService;
  private final UserService userService;

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

  public Collection<User> findAllUsers() {
    return userService.findAll();
  }

  public Collection<Trainee> findAllTrainees() {
    return traineeService.findAll();
  }

  public List<Trainer> listOfNotAssignedToTrainee(Long traineeId) {
    return trainerService.listOfNotAssignedToTrainee(traineeId);
  }
  public Boolean usernameToPasswordMatchingCheck(String username, String password) {
    return userService.usernameToPasswordMatchingCheck(username, password);
  }

  public List<Training> trainingsByUsernameOfTraineeAndCriteria(String username, String trainingTypeProperty, String trainingTypeValue) {
    return traineeService.trainingsByUsernameAndCriteria(username, trainingTypeProperty, trainingTypeValue);
  }

  public List<Training> trainingsByUsernameOfTrainerAndCriteria(String username, String trainingTypeProperty, String trainingTypeValue) {
    return trainerService.trainingsByUsernameAndCriteria(username, trainingTypeProperty, trainingTypeValue);
  }
  public Trainee toggleActivationNonIdempotent(Long id) {
    return traineeService.toggleActivationNonIdempotent(id);
  }
  public Trainer toggleActivationNonIdempotentTrainer(Long id) {
    return trainerService.toggleActivationNonIdempotent(id);
  }
}