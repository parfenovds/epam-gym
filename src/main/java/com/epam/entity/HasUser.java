package com.epam.entity;

public interface HasUser extends AbstractEntity {
  User getUser();
  void setUser(User user);
}
