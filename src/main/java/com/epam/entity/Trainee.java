package com.epam.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trainee implements AbstractEntity {
  private Long id;
  private LocalDate dateOfBirth;//TODO: think about date format
  private String address;
  private Long userId;
  private final Collection<Training> trainings = new ArrayList<>();
}
