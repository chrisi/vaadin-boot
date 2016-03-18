package net.gtidev.test.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Calendar {

  @Id
  @GeneratedValue
  private Long id;

  private String caption;
  private String style;
}
