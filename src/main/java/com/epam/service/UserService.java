package com.epam.service;

import com.epam.entity.User;
import com.epam.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
public class UserService extends BaseServiceImpl<User> {

  private final UserRepository userRepository;
  private final Validator validator;

  public UserService(UserRepository userRepository, Validator validator) {
    super(userRepository, User.class, validator);
    this.userRepository = userRepository;
    this.validator = validator;
  }

  @Transactional
  @Override
  public User save(User entity) {
    validateUser(entity);
    setUsernameForUser(entity);
    setPasswordForUser(entity);
    return userRepository.create(entity);
  }

  private void validateUser(User entity) {
    Set<ConstraintViolation<User>> violations = validator.validate(entity);
    if(!violations.isEmpty()) {
      log.error("User create failure: {}", violations);
      throw new IllegalArgumentException("User create failure: " + violations);
    }
  }

  private static void setPasswordForUser(User entity) {
    entity.setPassword(RandomStringUtils.randomAlphanumeric(10));
    log.info("Password for user generated and set");
  }

  private void setUsernameForUser(User entity) {
    Optional<Integer> currentSerialNumber = getCurrentSerialNumberForDuplicateUsername(entity);
    entity.setUsername(entity.getFirstName() + "." + entity.getLastName() +
        (currentSerialNumber.map(integer -> String.valueOf(integer + 1)).orElse(""))
    );
    log.info("Username for user set: {}", entity.getUsername());
  }

  private Optional<Integer> getCurrentSerialNumberForDuplicateUsername(User entity) {
    Stream<User> users = findAll().stream();

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

  @Transactional(readOnly = true)
  public Boolean usernameToPasswordMatchingCheck(String username, String password) {
    return userRepository.usernameToPasswordMatchingCheck(username, password);
  }
}
