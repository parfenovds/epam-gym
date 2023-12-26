package com.epam.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException(Class<?> clazz, Object object) {
    super(String.format("%s not found by property = %s", clazz.getSimpleName(), object));
  }
  public NotFoundException(String message) {
    super(message);
  }
}