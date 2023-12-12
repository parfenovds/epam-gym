package com.epam.repository;

import com.epam.commonDB.Storage;
import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.User;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    long id = storage.getNextUserId().getAndIncrement();
    entity.setId(id);
    storage.getUsers().put(id, entity);
    log.info("User created in repository: {}", entity);
    return entity;
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
    Stream<User> usersFromTrainees = storage.getTrainees().values().stream()
        .map(Trainee::getUser);

    Stream<User> usersFromTrainers = storage.getTrainers().values().stream()
        .map(Trainer::getUser);

    Optional<Integer> currentSerialNumber = Stream.concat(usersFromTrainees, usersFromTrainers)
        .filter(u -> u.getFirstName().equals(entity.getFirstName()) && u.getLastName().equals(entity.getLastName()))
        .map(User::getUsername)
        .map(s -> {
          Matcher matcher = Pattern.compile("\\d+$").matcher(s);
          return matcher.find() ? Integer.parseInt(matcher.group()) : null;
        })
        .filter(Objects::nonNull)
        .max(Integer::compareTo);

    log.info("Current serial number for duplicate username (if exists, otherwise null): {}", currentSerialNumber.orElse(null));
    return currentSerialNumber;
  }
}
