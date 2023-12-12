package com.epam.commonDB.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class StringObjectFactoryTest {

  @Test
  void testCreate() {
    StringObjectFactory factory = new StringObjectFactory();
    String inputString = "Hello, World!";
    Object result = factory.create(inputString);
    assertNotNull(result);
    assertTrue(result instanceof String);
    assertEquals(inputString, result);
  }
}
