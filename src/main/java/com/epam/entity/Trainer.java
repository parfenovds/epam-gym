package com.epam.entity;

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
public class Trainer implements AbstractEntity {
  private Long id;
  private Long trainingTypeId;
  private Long userId;
  private Collection<Training> trainings = new ArrayList<>();
}
