package com.epam.repository;

import com.epam.commonDB.Storage;
import com.epam.entity.User;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Log4j2
public class UserRepository implements BaseRepository<User> {
  private final Storage storage;

  @Override
  public User get(Long id) {
    User user = storage.getUsers().get(id);
    log.info("User acquired from repository: {}", user);
    return user;
  }

  @Override
  public User create(User entity) {
    checkIfUserHasFirstnameAndLastname(entity);
    setUsernameForUser(entity);
    setPasswordForUser(entity);
    User user = storage.getUsers().put(storage.getNextUserId().getAndIncrement(), entity);
    log.info("User created in repository: {}", user);
    return user;
  }

  private static void setPasswordForUser(User entity) {
    entity.setPassword(RandomStringUtils.randomAlphanumeric(10));
    log.info("Password for user generated and set");
  }

  private void checkIfUserHasFirstnameAndLastname(User entity) {
    if (entity.getFirstName() == null || entity.getLastName() == null) {
      log.error("User create failure: User must have first name and last name");
      throw new IllegalArgumentException("User must have first name and last name");
    }
  }

  private void setUsernameForUser(User entity) {
    Optional<Integer> currentSerialNumber = getCurrentSerialNumberForDuplicateUsername(entity);
    entity.setUsername(entity.getFirstName() + "." + entity.getLastName() +
        (currentSerialNumber.map(integer -> String.valueOf(integer + 1)).orElse(""))
    );
    log.info("Username for user set: {}", entity.getUsername());
  }

  private Optional<Integer> getCurrentSerialNumberForDuplicateUsername(User entity) {
    Optional<Integer> currentSerialNumber = Stream.concat(storage.getTrainees().values().stream(), storage.getTrainees().values().stream())
        .filter(u -> u.getUser().getFirstName().equals(entity.getFirstName()) && u.getUser().getLastName().equals(entity.getLastName()))
        .map(u -> u.getUser().getUsername())
        .map(s -> s.replaceAll("^.*?(\\d+)$", "$1"))
        .map(Integer::parseInt)
        .max(Integer::compareTo);
    log.info("Current serial number for duplicate username (if exists, otherwise null): {}", currentSerialNumber.orElse(null));
    return currentSerialNumber;
  }
}
