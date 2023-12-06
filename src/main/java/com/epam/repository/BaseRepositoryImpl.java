package com.epam.repository;


import com.epam.commonDB.Storage;
import com.epam.entity.AbstractEntity;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

//@Repository
@AllArgsConstructor
@NoArgsConstructor
public class BaseRepositoryImpl<E extends AbstractEntity> implements BaseRepository<E> {
//  protected final Map<Long, E> map = new HashMap<>();
//  public final AtomicLong id = new AtomicLong(0L);
  private Storage storage;
  protected Map<Long, E> map;
  private AtomicLong id;

//  @Autowired
  public BaseRepositoryImpl(Storage storage) {
    this.storage = storage;
  }

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

  public E update(E entity) {
    return map.put(entity.getId(), entity);
  }

  public void delete(E entity) {
    map.remove(entity.getId());
  }

  protected boolean nullOrEquals(Object patternField, Object repoField) {
    return patternField == null || patternField.equals(repoField);
  }
}