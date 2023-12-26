package com.epam.service;

import com.epam.entity.AbstractEntity;
import java.util.Collection;

public interface BaseService<E extends AbstractEntity> {
  E findById(Long id);

  void delete(Long id);

  E update(E entity);

  E save(E entity);

  Collection<E> findAll();

}
