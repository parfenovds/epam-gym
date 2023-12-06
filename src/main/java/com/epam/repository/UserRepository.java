package com.epam.repository;

import com.epam.commonDB.Storage;
import com.epam.entity.User;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserRepository extends BaseRepository<User> {

  private final Storage storage;

  @Override
  public Stream<User> find(User pattern) {
    return null;
  }

//  @Override
//  public Stream<User> find(User pattern) {
//    return map.values()
//        .stream()
//        .filter(u -> nullOrEquals(pattern.getId(), u.getId()))
//        .filter(u -> nullOrEquals(pattern.get, u.getLogin()))
//        .filter(u -> nullOrEquals(pattern.getPassword(), u.getPassword()))
//        .filter(u -> nullOrEquals(pattern.getRole(), u.getRole()));
//  }

}