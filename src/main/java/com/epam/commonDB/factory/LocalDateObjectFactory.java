package com.epam.commonDB.factory;

import java.time.LocalDate;

public class LocalDateObjectFactory implements ObjectFactory {
  @Override
  public Object create(String stringToBeConverted) {
    return LocalDate.parse(stringToBeConverted);
  }
}
