package com.epam.commonDB.factory;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongObjectFactory implements ObjectFactory {
  @Override
  public Object create(String stringToBeConverted) {
    return new AtomicLong(Long.parseLong(stringToBeConverted));
  }
}
