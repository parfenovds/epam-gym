package com.epam.service;

import com.epam.entity.AbstractEntity;

public interface BaseService<E extends AbstractEntity> {
  E findById(Long id);

  E save(E entity);

}
