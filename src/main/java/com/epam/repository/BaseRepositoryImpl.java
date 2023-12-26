package com.epam.repository;

import com.epam.entity.AbstractEntity;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;

@Log4j2
public abstract class BaseRepositoryImpl<E extends AbstractEntity> implements BaseRepository<E> {

  private final SessionFactory sessionFactory;
  private final Class<E> entityClass;

  public BaseRepositoryImpl(SessionFactory sessionFactory, Class<E> entityClass) {
    this.sessionFactory = sessionFactory;
    this.entityClass = entityClass;
  }

  @Override
  public Optional<E> get(Long id) {
    Optional<E> foundEntity = Optional.ofNullable(sessionFactory.getCurrentSession().get(entityClass, id));
    log.info("{} with ID: {} fetched", entityClass.getSimpleName(), id);
    return foundEntity;
  }

  @Override
  public void delete(Long id) {
    E entity = sessionFactory.getCurrentSession().get(entityClass, id);
    if (Objects.nonNull(entity)) {
      sessionFactory.getCurrentSession().remove(entity);
      log.info("{} with ID: {} delete", entityClass.getSimpleName(), id);
    }
  }

  @Override
  public E update(E entity) {
    E updatedEntity = sessionFactory.getCurrentSession().merge(entity);
    log.info("{} updated: {}", entityClass.getSimpleName(), updatedEntity);
    return updatedEntity;
  }

  @Override
  public E create(E entity) {
    sessionFactory.getCurrentSession().persist(entity);
    log.info("{} created: {}", entityClass.getSimpleName(), entity);
    return entity;
  }

  @Override
  public List<E> getAll() {
    List<E> resultList = sessionFactory.getCurrentSession()
        .createQuery("SELECT entity FROM " + entityClass.getSimpleName() + " entity ORDER BY entity.id", entityClass)
        .getResultList();
    log.info("Fetched all {} records: {}", entityClass.getSimpleName(), resultList);
    return resultList;
  }
}
