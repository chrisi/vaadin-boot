package net.gtidev.test.ui.view;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import net.gtidev.test.dbaccess.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventEditorPopup extends VerticalLayout {

  @Autowired
  private CalendarRepository calendarRepository;

  private void initFormFields(Layout formLayout, Class<? extends CalendarEvent> eventClass) {

    DateField startDateField = Utils.createDateField("Start date");
    DateField endDateField = Utils.createDateField("End date");

    final CheckBox allDayField = Utils.createCheckBox("All-day");
    allDayField.addValueChangeListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = -7104996493482558021L;

      @Override
      public void valueChange(Property.ValueChangeEvent event) {
        Object value = event.getProperty().getValue();
        if (value instanceof Boolean && Boolean.TRUE.equals(value)) {
          if (startDateField != null && endDateField != null) {
            startDateField.setResolution(Resolution.DAY);
            endDateField.setResolution(Resolution.DAY);
          }
        } else {
          if (startDateField != null && endDateField != null) {
            startDateField.setResolution(Resolution.MINUTE);
            endDateField.setResolution(Resolution.MINUTE);
          }
        }
      }

    });

    TextField captionField = Utils.createTextField("Caption");
    captionField.setInputPrompt("Event name");
    captionField.setRequired(true);
    final TextField whereField = Utils.createTextField("Where");
    whereField.setInputPrompt("Address or location");
    final TextArea descriptionField = Utils.createTextArea("Description");
    descriptionField.setInputPrompt("Describe the event");
    descriptionField.setRows(3);

    final ComboBox styleNameField = Utils.createComboBox("Calendar", String.class, "c", "");
    java.util.List<net.gtidev.test.model.Calendar> cals = calendarRepository.findAll();
    for (net.gtidev.test.model.Calendar cal : cals) {
      Item i = styleNameField.addItem(cal.getStyle());
      i.getItemProperty("c").setValue(cal.getCaption());
    }

    styleNameField.setInputPrompt("Choose calendar");
    styleNameField.setTextInputAllowed(false);

    formLayout.addComponent(startDateField);
    formLayout.addComponent(endDateField);
    formLayout.addComponent(allDayField);
    formLayout.addComponent(captionField);
    formLayout.addComponent(descriptionField);
    formLayout.addComponent(styleNameField);

//    scheduleEventFieldGroup.bind(startDateField, "start");
//    scheduleEventFieldGroup.bind(endDateField, "end");
//    scheduleEventFieldGroup.bind(captionField, "caption");
//    scheduleEventFieldGroup.bind(descriptionField, "description");
//    scheduleEventFieldGroup.bind(styleNameField, "styleName");
//    scheduleEventFieldGroup.bind(allDayField, "allDay");
  }

}
