package com.epam.commonDB.factory;

import java.util.concurrent.atomic.AtomicLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class AtomicLongObjectFactoryTest {

  @Test
  void testCreateValidString() {
    String validString = "12345";
    AtomicLongObjectFactory factory = new AtomicLongObjectFactory();
    Object result = factory.create(validString);
    assertTrue(result instanceof AtomicLong);
    assertEquals(12345L, ((AtomicLong) result).get());
  }

  @Test
  void testCreateInvalidString() {
    String invalidString = "abc";
    AtomicLongObjectFactory factory = new AtomicLongObjectFactory();
    assertThrows(NumberFormatException.class, () -> factory.create(invalidString));
  }
}