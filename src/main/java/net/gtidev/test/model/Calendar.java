package net.gtidev.test.model;

import lombok.Data;
import net.gtidev.test.DropDown;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Calendar implements DropDown<String, String> {

  @Id
  @GeneratedValue
  private Long id;

  private String caption;
  private String style;

  @Override
  public String getKey() {
    return style;
  }

  @Override
  public String getValue() {
    return caption;
  }
}
