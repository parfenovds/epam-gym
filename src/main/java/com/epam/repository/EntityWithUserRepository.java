package com.epam.repository;

import com.epam.entity.HasUser;
import com.epam.entity.Training;
import com.epam.exception.NotFoundException;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaJoin;
import org.hibernate.query.criteria.JpaRoot;

@Log4j2
public abstract class EntityWithUserRepository<E extends HasUser> extends BaseRepositoryImpl<E> {
  private final SessionFactory sessionFactory;
  private final Class<E> entityClass;
  public EntityWithUserRepository(SessionFactory sessionFactory, Class<E> entityClass) {
    super(sessionFactory, entityClass);
    this.sessionFactory = sessionFactory;
    this.entityClass = entityClass;
  }
  public Optional<E> getByUsername(String username) {
    Optional<E> byUsername = sessionFactory.getCurrentSession()
        .createQuery("SELECT entity FROM " + entityClass.getName() + " entity WHERE entity.user.username = :username", entityClass)
        .setParameter("username", username)
        .uniqueResultOptional();
    log.info("{} with username: {} fetched", entityClass.getSimpleName(), username);
    return byUsername;
  }
  public E changePassword(Long id, String password) {
    E entity = get(id).orElseThrow(() -> new NotFoundException(entityClass, id));
    entity.getUser().setPassword(password);
    log.info("password changed for {}", entity);
    return sessionFactory.getCurrentSession().merge(entity);
  }

  public E activate(Long id) {
    E entity = toggleActivation(id, true);
    log.info("{} activated", entity);
    return entity;
  }

  public E deactivate(Long id) {
    E entity = toggleActivation(id, false);
    log.info("{} deactivated", entity);
    return entity;
  }

  private E toggleActivation(Long id, Boolean isActive) {
    E entity = get(id).orElseThrow(() -> new NotFoundException(entityClass, id));
    entity.getUser().setIsActive(isActive);
    return sessionFactory.getCurrentSession().merge(entity);
  }

  public E toggleActivationNonIdempotent(Long id) {
    E entity = get(id).orElseThrow(() -> new NotFoundException(entityClass, id));
    entity.getUser().setIsActive(!entity.getUser().getIsActive());
    E merged = sessionFactory.getCurrentSession().merge(entity);
    log.info("activation toggled for {}", merged);
    return merged;
  }

  public List<Training> trainingsByUsernameAndCriteria(String username, String trainingTypeProperty, String trainingTypeValue) {
    HibernateCriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
    JpaCriteriaQuery<Training> query = criteriaBuilder.createQuery(Training.class);
    JpaRoot<Training> from = query.from(Training.class);
    JpaJoin<Training, E> entityJoin = from.join(entityClass.getSimpleName().toLowerCase());
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(criteriaBuilder.equal(entityJoin.get("user").get("username"), username));
    predicates.add(criteriaBuilder.equal(from.get(trainingTypeProperty), trainingTypeValue));
    query.select(from).where(predicates.toArray(new Predicate[0]));
    List<Training> resultList = sessionFactory.getCurrentSession().createQuery(query).getResultList();
    log.info("Fetched all {} records by username and criteria: {}", entityClass.getSimpleName(), resultList);
    return resultList;
  }
}
