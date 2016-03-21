package net.gtidev.test.ui.view;

import com.vaadin.data.Item;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import net.gtidev.test.DropDown;

import java.util.List;

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

  /**
   * Erstellt eine ComboBox-Komponente.
   *
   * @param caption    Die angezeigte Beschriftung der Komponente
   * @param propClass  Der Value-Typ der Items, typischerweise String.class
   * @param dropDown   Die Liste von Objekten, welche im Dropdown angezeigt werden soll
   * @param defaultVal Der Ausgangswert, der Angezeit wird, wenn nichts gewählt ist
   * @param <K>        der Typ der Key-Komponente
   * @param <V>        der Typ der Value-Komponente (der angezeite Wert)
   * @return die fertig erzeugte Dropdown-Komponente
   */
  public static <K, V> ComboBox createComboBox(String caption, Class<V> propClass, List<? extends DropDown<K, V>> dropDown, String defaultVal) {
    ComboBox s = new ComboBox(caption);
    s.addContainerProperty("name", propClass, defaultVal);
    s.setItemCaptionPropertyId("name");
    for (DropDown itm : dropDown) {
      Item i = s.addItem(itm.getKey());
      i.getItemProperty("name").setValue(itm.getValue());
    }
    return s;
  }

  /**
   * Erstellt eine ComboBox-Komponente.
   *
   * @param caption  Die angezeigte Beschriftung der Komponente
   * @param dropDown Die Liste von Objekten, welche im Dropdown angezeigt werden soll
   * @param <K>      der Typ der Key-Komponente
   * @param <V>      der Typ der Value-Komponente (der angezeite Wert)
   * @return die fertig erzeugte Dropdown-Komponente
   */
  public static <K, V> ComboBox createComboBox(String caption, List<? extends DropDown<K, V>> dropDown) {
    ComboBox s = new ComboBox(caption);
    s.addContainerProperty("name", String.class, "");
    s.setItemCaptionPropertyId("name");
    for (DropDown itm : dropDown) {
      Item i = s.addItem(itm.getKey());
      i.getItemProperty("name").setValue(itm.getValue());
    }
    return s;
  }

  /**
   * Erstellt eine ComboBox-Komponente, in welcher der Item-Key das Pojo selbst ist.<br>
   * Die Key-Komponente des {@link DropDown}-Interfaces wird hier nicht beruecksichtigt.
   *
   * @param caption    Die angezeigte Beschriftung der Komponente
   * @param propClass  Der Value-Typ der Items, typischerweise String.class
   * @param dropDown   Die Liste von Objekten, welche im Dropdown angezeigt werden soll
   * @param defaultVal Der Ausgangswert, der Angezeit wird, wenn nichts gewählt ist
   * @param <K>        der Typ der Key-Komponente
   * @param <V>        der Typ der Value-Komponente (der angezeite Wert)
   * @return die fertig erzeugte Dropdown-Komponente
   */
  public static <K, V> ComboBox createObjectComboBox(String caption, Class<V> propClass, List<? extends DropDown<K, V>> dropDown, String defaultVal) {
    ComboBox s = new ComboBox(caption);
    s.addContainerProperty("name", propClass, defaultVal);
    s.setItemCaptionPropertyId("name");
    for (DropDown itm : dropDown) {
      Item i = s.addItem(itm);
      i.getItemProperty("name").setValue(itm.getValue());
    }
    return s;
  }

  /**
   * Erstellt eine ComboBox-Komponente, in welcher der Item-Key das Pojo selbst ist.<br>
   * Die Key-Komponente des {@link DropDown}-Interfaces wird hier nicht beruecksichtigt.
   *
   * @param caption  Die angezeigte Beschriftung der Komponente
   * @param dropDown Die Liste von Objekten, welche im Dropdown angezeigt werden soll
   * @param <K>      der Typ der Key-Komponente (wird hier nicht beruecksichtigt)
   * @param <V>      der Typ der Value-Komponente (der angezeite Wert)
   * @return die fertig erzeugte Dropdown-Komponente
   */
  public static <K, V> ComboBox createObjectComboBox(String caption, List<? extends DropDown<K, V>> dropDown) {
    ComboBox s = new ComboBox(caption);
    s.addContainerProperty("name", String.class, "");
    s.setItemCaptionPropertyId("name");
    for (DropDown itm : dropDown) {
      Item i = s.addItem(itm);
      i.getItemProperty("name").setValue(itm.getValue());
    }
    return s;
  }
}
