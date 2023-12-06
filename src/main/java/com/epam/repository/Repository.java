package com.epam.repository;
import java.util.Collection;
import java.util.stream.Stream;

public interface Repository<E> {

  Collection<E> getAll();

  Stream<E> find(E pattern);

  E get(Long id);

  E create(E entity);

  E update(E entity);

  void delete(E entity);
}