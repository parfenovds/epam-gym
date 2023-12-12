package com.epam.service;

import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.User;
import com.epam.repository.UserRepository;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@AllArgsConstructor
public class UserService implements BaseService<User> {

  private final UserRepository userRepository;
  @Override
  public User findById(Long id) {
    return userRepository.get(id);
  }

  @Override
  public User save(User entity) {
    checkIfUserHasFirstnameAndLastname(entity);
    setUsernameForUser(entity);
    setPasswordForUser(entity);
    return userRepository.create(entity);
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
    Stream<User> users = userRepository.getAll().stream();

    Optional<Integer> currentSerialNumber = users
        .filter(u -> u.getFirstName().equals(entity.getFirstName()) && u.getLastName().equals(entity.getLastName()))
        .map(User::getUsername)
        .map(s -> {
          Matcher matcher = Pattern.compile("\\d+$").matcher(s);
          return matcher.find() ? Integer.parseInt(matcher.group()) : 0;
        })
        .max(Integer::compareTo);

    log.info("Current serial number for duplicate username (if exists, otherwise null): {}", currentSerialNumber.orElse(null));
    return currentSerialNumber;
  }
}
