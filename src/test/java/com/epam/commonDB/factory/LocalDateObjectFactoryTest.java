package com.epam.commonDB.factory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class LocalDateObjectFactoryTest {

  @Test
  void testCreateValidDateString() {
    String validDateString = "2023-12-10";
    LocalDateObjectFactory factory = new LocalDateObjectFactory();
    Object result = factory.create(validDateString);
    assertTrue(result instanceof LocalDate);
    assertEquals(LocalDate.parse(validDateString), result);
  }

  @Test
  void testCreateInvalidDateString() {
    String invalidDateString = "invalid";
    LocalDateObjectFactory factory = new LocalDateObjectFactory();
    assertThrows(DateTimeParseException.class, () -> factory.create(invalidDateString));
  }
}
