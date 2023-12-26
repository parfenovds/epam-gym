package com.epam.service;

import com.epam.entity.AbstractEntity;
import com.epam.repository.BaseRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Collection;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;


@AllArgsConstructor
@Log4j2
public abstract class BaseServiceImpl<E extends AbstractEntity> implements BaseService<E> {

  private final BaseRepository<E> baseRepository;
  private final Class<E> entityClass;
  private final Validator validator;

  @Transactional(readOnly = true)
  @Override
  public E findById(Long id) {
    E entity = baseRepository.get(id).orElseThrow(() -> new RuntimeException("Entity not found"));
    log.info("{} acquired from service: {}", entityClass.getSimpleName(), entity);
    return entity;
  }

  @Transactional
  @Override
  public void delete(Long id) {
    baseRepository.delete(id);
    log.info("{} deleted from service: {}", entityClass.getSimpleName(), id);
  }

  @Transactional
  @Override
  public E update(E entity) {
    validateEntity(entity);
    E updated = baseRepository.update(entity);
    log.info("{} updated in service: {}", entityClass.getSimpleName(), updated);
    return updated;
  }

  @Transactional
  @Override
  public E save(E entity) {
    validateEntity(entity);
    E saved = baseRepository.create(entity);
    log.info("{} saved in service: {}", entityClass.getSimpleName(), saved);
    return saved;
  }

  @Transactional(readOnly = true)
  @Override
  public Collection<E> findAll() {
    Collection<E> entities = baseRepository.getAll();
    log.info("{}s acquired from service: {}", entityClass.getSimpleName(), entities);
    return entities;
  }

  private void validateEntity(E entity) {
    Set<ConstraintViolation<E>> violations = validator.validate(entity);
    if (!violations.isEmpty()) {
      log.error("Entity create failure: {}", violations);
      throw new IllegalArgumentException("Entity create failure: " + violations);
    }
  }
}
