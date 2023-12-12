package com.epam.commonDB.factory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BooleanObjectFactoryTest {

  @Test
  void testCreateTrueString() {
    String trueString = "true";
    BooleanObjectFactory factory = new BooleanObjectFactory();
    Object result = factory.create(trueString);
    assertTrue(result instanceof Boolean);
    assertTrue((Boolean) result);
  }

  @Test
  void testCreateFalseString() {
    String falseString = "false";
    BooleanObjectFactory factory = new BooleanObjectFactory();
    Object result = factory.create(falseString);
    assertTrue(result instanceof Boolean);
    assertFalse((Boolean) result);
  }
}
