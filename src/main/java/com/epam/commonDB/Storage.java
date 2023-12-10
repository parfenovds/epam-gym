package com.epam.commonDB;

import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.Training;
import com.epam.entity.TrainingType;
import com.epam.entity.User;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Storage {
  private AtomicLong nextTraineeId = new AtomicLong(1L);
  private AtomicLong nextTrainerId = new AtomicLong(1L);
  private AtomicLong nextTrainingId = new AtomicLong(1L);
  private AtomicLong nextTrainingTypeId = new AtomicLong(1L);
  private AtomicLong nextUserId = new AtomicLong(1L);

  private Map<Long, Trainee> trainees = new HashMap<>();
  private Map<Long, Trainer> trainers = new HashMap<>();
  private Map<Long, Training> trainings = new HashMap<>();
  private Map<Long, TrainingType> trainingTypes = new HashMap<>();
  private Map<Long, User> users = new HashMap<>();

//  public <T> Map<Long, T> getMapByClass(Class<? extends AbstractEntity> clazz) {
//    try {
//      return (Map<Long, T>) clazz.getMethod("get" + clazz.getSimpleName() + "s").invoke(this);
//    } catch (IllegalAccessException e) {
//      throw new RuntimeException(e);
//    } catch (InvocationTargetException e) {
//      throw new RuntimeException(e);
//    } catch (NoSuchMethodException e) {
//      throw new RuntimeException(e);
//    }
//  }
//  public AtomicLong getAtomicLongByClass(Class<? extends AbstractEntity> clazz) {
//    try {
//      return (AtomicLong) clazz.getMethod("getNext" + clazz.getSimpleName() + "Id").invoke(this);
//    } catch (IllegalAccessException e) {
//      throw new RuntimeException(e);
//    } catch (InvocationTargetException e) {
//      throw new RuntimeException(e);
//    } catch (NoSuchMethodException e) {
//      throw new RuntimeException(e);
//    }
//  }
}
