package com.epam.commonDB.factory;

public class StringObjectFactory implements ObjectFactory{
  @Override
  public Object create(String stringToBeConverted) {
    return stringToBeConverted;
  }
}
