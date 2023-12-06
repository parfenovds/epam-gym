package com.epam.repository;


import com.epam.entity.AbstractEntity;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public abstract class BaseRepository<E extends AbstractEntity> implements Repository<E> {
  protected final Map<Long, E> map = new HashMap<>();
  public final AtomicLong id = new AtomicLong(0L);

  @Override
  public Collection<E> getAll() {
    return map.values();
  }

  @Override
  public E get(Long id) {
    return map.get(id);
  }

  @Override
  public E create(E entity) {
    entity.setId(id.incrementAndGet());
    return update(entity);
  }

  @Override
  public E update(E entity) {
    return map.put(entity.getId(), entity);
  }

  @Override
  public void delete(E entity) {
    map.remove(entity.getId());
  }

  protected boolean nullOrEquals(Object patternField, Object repoField) {
    return patternField == null || patternField.equals(repoField);
  }
}