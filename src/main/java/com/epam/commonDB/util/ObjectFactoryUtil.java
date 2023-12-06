package com.epam.commonDB.util;

import com.epam.commonDB.factory.AtomicLongObjectFactory;
import com.epam.commonDB.factory.BooleanObjectFactory;
import com.epam.commonDB.factory.LocalDateObjectFactory;
import com.epam.commonDB.factory.LongObjectFactory;
import com.epam.commonDB.factory.ObjectFactory;
import com.epam.commonDB.factory.StringObjectFactory;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectFactoryUtil {
  public static <T> ObjectFactory getObjectFactory(Class<T> clazz) {
    if (clazz == String.class) {
      return new StringObjectFactory();
    } else if (clazz == Long.class) {
      return new LongObjectFactory();
    } else if (clazz == Boolean.class) {
      return new BooleanObjectFactory();
    } else if (clazz == LocalDate.class) {
      return new LocalDateObjectFactory();
    } else if(clazz == AtomicLong.class) {
      return new AtomicLongObjectFactory();
    }
    throw new RuntimeException("Unsupported type: " + clazz);
  }
}
