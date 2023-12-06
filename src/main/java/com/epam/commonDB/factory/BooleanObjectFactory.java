package com.epam.commonDB.factory;

public class BooleanObjectFactory implements ObjectFactory {
  @Override
  public Object create(String stringToBeConverted) {
    return Boolean.valueOf(stringToBeConverted);
  }
}
