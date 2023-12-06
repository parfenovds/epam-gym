package com.epam.repository;

public interface BaseRepository<E> {

  E get(Long id);

  E create(E entity);

}