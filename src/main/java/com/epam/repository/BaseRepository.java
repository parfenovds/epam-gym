package com.epam.repository;

import com.epam.entity.AbstractEntity;
import java.util.List;
import java.util.Optional;

public interface BaseRepository<E extends AbstractEntity> {
  Optional<E> get(Long id);

  E update(E entity);

  E create(E entity);

  void delete(Long id);

  List<E> getAll();
}
