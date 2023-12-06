package com.epam.service;

import com.epam.entity.AbstractEntity;
import java.io.Serializable;
import java.util.List;

public interface BaseService<E extends AbstractEntity> {
  E findById(Long id);

  E save(E entity);

}
