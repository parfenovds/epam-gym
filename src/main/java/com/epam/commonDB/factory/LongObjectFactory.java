package com.epam.commonDB.factory;

public class LongObjectFactory implements ObjectFactory {
  @Override
  public Object create(String stringToBeConverted) {
    return Long.valueOf(stringToBeConverted);
  }
}
