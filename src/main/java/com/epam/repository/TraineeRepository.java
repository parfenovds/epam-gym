package com.epam.repository;

import com.epam.entity.Trainee;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeRepository extends EntityWithUserRepository<Trainee>{
  private final SessionFactory sessionFactory;
  public TraineeRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Trainee.class);
    this.sessionFactory = sessionFactory;
  }
}
