package com.epam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements AbstractEntity {
  private Long id;
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private Boolean isActive;
}
