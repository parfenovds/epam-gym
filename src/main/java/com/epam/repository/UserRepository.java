package com.epam.repository;

import com.epam.commonDB.Storage;
import com.epam.entity.User;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserRepository implements BaseRepository<User> {
  private final Storage storage;

  @Override
  public User get(Long id) {
    return storage.getUsers().get(id);
  }

  @Override
  public User create(User entity) {
    checkIfUserHasFirstnameAndLastname(entity);
    setUsernameForUser(entity);
    entity.setPassword(RandomStringUtils.randomAlphanumeric(10));
    return storage.getUsers().put(storage.getNextUserId().getAndIncrement(), entity);
  }

  private void setUsernameForUser(User entity) {
    Optional<Integer> currentSerialNumber = getCurrentSerialNumberForDuplicateUsername(entity);
    entity.setUsername(entity.getFirstName() + "." + entity.getLastName() +
        (currentSerialNumber.map(integer -> String.valueOf(integer + 1)).orElse(""))
    );
  }

  private Optional<Integer> getCurrentSerialNumberForDuplicateUsername(User entity) {
    Optional<Integer> currentSerialNumber = Stream.concat(storage.getTrainees().values().stream(), storage.getTrainees().values().stream())
        .filter(u -> u.getUser().getFirstName().equals(entity.getFirstName()) && u.getUser().getLastName().equals(entity.getLastName()))
        .map(u -> u.getUser().getUsername())
        .map(s -> s.replaceAll("^.*?(\\d+)$", "$1"))
        .map(Integer::parseInt)
        .max(Integer::compareTo);
    return currentSerialNumber;
  }

  private static void checkIfUserHasFirstnameAndLastname(User entity) {
    if (entity.getFirstName() == null || entity.getLastName() == null) {
      throw new IllegalArgumentException("User must have first name and last name");
    }
  }

  public User update(User entity) {
    return storage.getUsers().put(entity.getId(), entity);
  }

  public void delete(Long id) {
    storage.getUsers().remove(id);
  }
}
