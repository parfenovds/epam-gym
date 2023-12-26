package com.epam.exception;

import com.epam.entity.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotFoundExceptionTest {

  @Test
  public void testNotFoundExceptionWithClassAndObject() {
    Class<?> clazz = User.class;
    Object object = 123;
    NotFoundException exception = new NotFoundException(clazz, object);

    String expectedMessage = "User not found by property = 123";
    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  public void testNotFoundExceptionWithMessage() {
    String message = "Custom not found message";
    NotFoundException exception = new NotFoundException(message);

    assertEquals(message, exception.getMessage());
  }
}