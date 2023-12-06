package com.epam.entity;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Training implements AbstractEntity {
  private Long id;
  private Long traineeId;
  private Long trainerId;
  private String trainingName;
  private Long trainingTypeId;
  private LocalDate trainingDate;//TODO: think about date format
  private Long trainingDuration;
}
