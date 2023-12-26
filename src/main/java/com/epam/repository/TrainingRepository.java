package com.epam.repository;

import com.epam.entity.Training;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingRepository extends BaseRepositoryImpl<Training>{
  public TrainingRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Training.class);
  }
}
