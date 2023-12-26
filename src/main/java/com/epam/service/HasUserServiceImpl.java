package com.epam.service;

import com.epam.entity.HasUser;
import com.epam.entity.Training;
import com.epam.exception.NotFoundException;
import com.epam.repository.EntityWithUserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
public abstract class HasUserServiceImpl<E extends HasUser> extends BaseServiceImpl<E> {

  private final EntityWithUserRepository<E> baseRepository;
  private final Class<E> entityClass;
  private final Validator validator;
  public HasUserServiceImpl(EntityWithUserRepository<E> baseRepository, Class<E> entityClass, Validator validator) {
    super(baseRepository, entityClass, validator);
    this.baseRepository = baseRepository;
    this.entityClass = entityClass;
    this.validator = validator;
  }
  @Transactional
  @Override
  public E save(E entity) {
    validateEntity(entity);
    E savedEntity = baseRepository.create(entity);
    log.info("{} saved in service: {}", entityClass.getSimpleName(), savedEntity);
    return savedEntity;
  }

  private void validateEntity(E entity) {
    Set<ConstraintViolation<E>> violations = validator.validate(entity);
    if (!violations.isEmpty()) {
      log.error("Entity create failure: {}", violations);
      throw new IllegalArgumentException("Entity create failure: " + violations);
    }
  }

  @Transactional(readOnly = true)
  public E findByUsername(String username) {
    E entity = baseRepository.getByUsername(username).orElseThrow(() -> new NotFoundException(entityClass, username));
    log.info("{} acquired from service: {}", entityClass.getSimpleName(), entity);
    return entity;
  }

  @Transactional
  public E changePassword(Long id, String password) {
    E entity = findById(id);
    entity.getUser().setPassword(password);
    E updated = baseRepository.update(entity);
    log.info("password changed in service for {}", updated);
    return updated;
  }

  @Transactional
  public E activate(Long id) {
    E activated = baseRepository.activate(id);
    log.info("{} activated in service", activated);
    return activated;
  }

  @Transactional
  public E deactivate(Long id) {
    E deactivated = baseRepository.deactivate(id);
    log.info("{} deactivated in service", deactivated);
    return deactivated;
  }

  @Transactional
  public E toggleActivationNonIdempotent(Long id) {
    E entity = baseRepository.toggleActivationNonIdempotent(id);
    log.info("activation toggled in service for {}", entity);
    return entity;
  }

  @Transactional(readOnly = true)
  public List<Training> trainingsByUsernameAndCriteria(String username, String trainingTypeProperty, String trainingTypeValue) {
    List<Training> trainings = baseRepository.trainingsByUsernameAndCriteria(username, trainingTypeProperty, trainingTypeValue);
    log.info("trainings acquired from service by username and criteria: {}", trainings);
    return trainings;
  }
}
