package net.gtidev.test.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.gtidev.test.DropDown;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@EqualsAndHashCode(of = {"id"})
public class Project implements DropDown<Long, String> {
  @Id
  @GeneratedValue
  private Long id;

  private String name;
  private String description;
  private boolean billable;
  private String color;

  @Override
  public Long getKey() {
    return id;
  }

  @Override
  public String getValue() {
    return name;
  }
}
