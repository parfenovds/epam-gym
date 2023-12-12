package com.epam.commonDB.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class LongObjectFactoryTest {

  @Test
  void testCreateValidLongString() {
    String validLongString = "12345";
    LongObjectFactory factory = new LongObjectFactory();
    Object result = factory.create(validLongString);
    assertTrue(result instanceof Long);
    assertEquals(Long.valueOf(validLongString), result);
  }

  @Test
  void testCreateInvalidLongString() {
    String invalidLongString = "invalid";
    LongObjectFactory factory = new LongObjectFactory();
    assertThrows(NumberFormatException.class, () -> factory.create(invalidLongString));
  }
}