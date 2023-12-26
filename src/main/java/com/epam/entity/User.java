package com.epam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"username"})
@Entity
@Table(name = "users")
public class User implements AbstractEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @NotNull
  @Column(name = "first_name", nullable = false)
  private String firstName;
  @NotNull
  @Column(name = "last_name", nullable = false)
  private String lastName;
  @NotNull
  @Column(name = "username", nullable = false)
  private String username;
  @NotNull
  @Column(name = "password", nullable = false)
  private String password;
  @NotNull
  @Column(name = "is_active", nullable = false)
  private Boolean isActive;
}
