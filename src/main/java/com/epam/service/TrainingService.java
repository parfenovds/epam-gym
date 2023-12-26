package com.epam.service;

import com.epam.entity.Training;
import com.epam.repository.BaseRepository;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

@Service
public class TrainingService extends BaseServiceImpl<Training>{
  public TrainingService(BaseRepository<Training> baseRepository, Validator validator) {
    super(baseRepository, Training.class, validator);
  }
}
