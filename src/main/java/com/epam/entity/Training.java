package com.epam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "training")
public class Training implements AbstractEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Valid
  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trainee_id", nullable = false)
  private Trainee trainee;
  @Valid
  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trainer_id", nullable = false)
  private Trainer trainer;
  @NotNull
  @Column(name = "training_name", nullable = false)
  private String trainingName;
  @NotNull
  @ManyToOne
  @JoinColumn(name = "training_type_id", nullable = false)
  private TrainingType trainingType;
  @NotNull
  @Column(name = "training_date", nullable = false)
  private LocalDate trainingDate;
  @NotNull
  @Column(name = "training_duration", nullable = false)
  private Long trainingDuration;

  @Override
  public String toString() {
    return "Training{" +
        "id=" + id +
        ", traineeId=" + trainee.getId() +
        ", trainerId=" + trainer.getId() +
        ", trainingName='" + trainingName + '\'' +
        ", trainingType=" + trainingType +
        ", trainingDate=" + trainingDate +
        ", trainingDuration=" + trainingDuration +
        '}';
  }
}
