package com.epam.commonDB.util;

import com.epam.commonDB.factory.AtomicLongObjectFactory;
import com.epam.commonDB.factory.BooleanObjectFactory;
import com.epam.commonDB.factory.LocalDateObjectFactory;
import com.epam.commonDB.factory.LongObjectFactory;
import com.epam.commonDB.factory.ObjectFactory;
import com.epam.commonDB.factory.StringObjectFactory;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ObjectFactoryUtilTest {

  @Test
  void testGetStringObjectFactory() {
    ObjectFactory factory = ObjectFactoryUtil.getObjectFactory(String.class);
    assertTrue(factory instanceof StringObjectFactory);
  }

  @Test
  void testGetLongObjectFactory() {
    ObjectFactory factory = ObjectFactoryUtil.getObjectFactory(Long.class);
    assertTrue(factory instanceof LongObjectFactory);
  }

  @Test
  void testGetBooleanObjectFactory() {
    ObjectFactory factory = ObjectFactoryUtil.getObjectFactory(Boolean.class);
    assertTrue(factory instanceof BooleanObjectFactory);
  }

  @Test
  void testGetLocalDateObjectFactory() {
    ObjectFactory factory = ObjectFactoryUtil.getObjectFactory(LocalDate.class);
    assertTrue(factory instanceof LocalDateObjectFactory);
  }

  @Test
  void testGetAtomicLongObjectFactory() {
    ObjectFactory factory = ObjectFactoryUtil.getObjectFactory(AtomicLong.class);
    assertTrue(factory instanceof AtomicLongObjectFactory);
  }

  @Test
  void testGetUnsupportedType() {
    assertThrows(RuntimeException.class, () -> ObjectFactoryUtil.getObjectFactory(Integer.class));
  }
}