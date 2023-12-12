package com.epam.repository;

import com.epam.commonDB.Storage;
import com.epam.entity.User;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Log4j2
public class UserRepository implements BaseRepository<User> {
  private final Storage storage;

  public Collection<User> getAll() {
    Collection<User> users = storage.getUsers().values();
    log.info("Users acquired from repository: {}", users);
    return users;
  }

  @Override
  public User get(Long id) {
    User user = storage.getUsers().get(id);
    log.info("User acquired from repository: {}", user);
    return user;
  }

  @Override
  public User create(User entity) {
    long id = storage.getNextUserId().getAndIncrement();
    entity.setId(id);
    storage.getUsers().put(id, entity);
    log.info("User created in repository: {}", entity);
    return entity;
  }
}
