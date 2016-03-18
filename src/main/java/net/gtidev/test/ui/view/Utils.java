package net.gtidev.test.ui.view;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;

public class Utils {

  public static CheckBox createCheckBox(String caption) {
    CheckBox cb = new CheckBox(caption);
    cb.setImmediate(true);
    return cb;
  }

  public static TextField createTextField(String caption) {
    TextField f = new TextField(caption);
    f.setNullRepresentation("");
    return f;
  }

  public static TextArea createTextArea(String caption) {
    TextArea f = new TextArea(caption);
    f.setNullRepresentation("");
    return f;
  }

  public static DateField createDateField(String caption) {
    DateField f = new DateField(caption);
    f.setResolution(Resolution.MINUTE);
    return f;
  }

  public static ComboBox createComboBox(String caption, Class<?> propClass, String propName, String defaultVal) {
    ComboBox s = new ComboBox(caption);
    s.addContainerProperty(propName, propClass, defaultVal);
    s.setItemCaptionPropertyId(propName);
    return s;
  }
}
