package com.epam.repository;

import com.epam.entity.Trainer;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerRepository extends EntityWithUserRepository<Trainer> {
private final SessionFactory sessionFactory;
  public TrainerRepository(SessionFactory sessionFactory) {
    super(sessionFactory, Trainer.class);
    this.sessionFactory = sessionFactory;
  }

  public List<Trainer> listOfNotAssignedToTrainee(Long traineeId) {
    return sessionFactory.getCurrentSession()
        .createQuery("SELECT DISTINCT t FROM Trainer t LEFT JOIN FETCH t.trainings tr WHERE t.user.isActive = true AND t.id NOT IN (SELECT tr.trainer.id FROM Training tr WHERE tr.trainee.id = :traineeId)", Trainer.class)
        .setParameter("traineeId", traineeId)
        .getResultList();
  }
}
