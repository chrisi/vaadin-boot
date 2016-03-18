package net.gtidev.test.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@EqualsAndHashCode(of = {"id"})
public class Project {
  @Id
  @GeneratedValue
  private Long id;

  private String name;
  private String description;
  private boolean billable;
}
