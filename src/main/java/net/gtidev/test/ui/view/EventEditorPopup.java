package net.gtidev.test.ui.view;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import lombok.Setter;
import net.gtidev.test.dbaccess.CalendarRepository;
import net.gtidev.test.dbaccess.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ViewScope
public class EventEditorPopup extends VerticalLayout {

  @Autowired
  private CalendarRepository calendarRepository;

  @Autowired
  private ProjectRepository projectRepository;

  private Window scheduleEventPopup;
  private final FormLayout scheduleEventFieldLayout = new FormLayout();

  private FieldGroup scheduleEventFieldGroup = new FieldGroup();

  private Button applyEventButton;
  private Button deleteEventButton;

  @Setter
  private CalendarEventListener listener;

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

    final ComboBox styleNameField = Utils.createComboBox("Calendar", calendarRepository.findAll());
    final ComboBox projectField = Utils.createObjectComboBox("Project", projectRepository.findAll());

    styleNameField.setInputPrompt("Choose calendar");
    styleNameField.setTextInputAllowed(false);

    formLayout.addComponent(startDateField);
    formLayout.addComponent(endDateField);
    formLayout.addComponent(allDayField);
    formLayout.addComponent(captionField);
    formLayout.addComponent(descriptionField);
    formLayout.addComponent(projectField);

    scheduleEventFieldGroup.bind(startDateField, "start");
    scheduleEventFieldGroup.bind(endDateField, "end");
    scheduleEventFieldGroup.bind(captionField, "caption");
    scheduleEventFieldGroup.bind(descriptionField, "description");
    scheduleEventFieldGroup.bind(projectField, "project");
    scheduleEventFieldGroup.bind(allDayField, "allDay");
  }

  private void createCalendarEventPopup() {
    VerticalLayout layout = new VerticalLayout();
    // layout.setMargin(true);
    layout.setSpacing(true);

    scheduleEventPopup = new Window(null, layout);
    scheduleEventPopup.setWidth("300px");
    scheduleEventPopup.setModal(true);
    scheduleEventPopup.center();

    scheduleEventFieldLayout.addStyleName("light");
    scheduleEventFieldLayout.setMargin(false);
    layout.addComponent(scheduleEventFieldLayout);

    applyEventButton = new Button("Apply", new Button.ClickListener() {

      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(Button.ClickEvent event) {
        try {
          scheduleEventFieldGroup.commit();
          listener.commitCalendarEvent();
          listener.getUI().removeWindow(scheduleEventPopup);
        } catch (FieldGroup.CommitException e) {
          e.printStackTrace();
        }
      }
    });
    applyEventButton.addStyleName("primary");
    Button cancel = new Button("Cancel", new Button.ClickListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(Button.ClickEvent event) {
        scheduleEventFieldGroup.discard();
        listener.discardCalendarEvent();
        listener.getUI().removeWindow(scheduleEventPopup);
      }
    });
    deleteEventButton = new Button("Delete", new Button.ClickListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void buttonClick(Button.ClickEvent event) {
        listener.deleteCalendarEvent();
        listener.getUI().removeWindow(scheduleEventPopup);
      }
    });
    deleteEventButton.addStyleName("borderless");
    scheduleEventPopup.addCloseListener(new Window.CloseListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void windowClose(Window.CloseEvent e) {
        scheduleEventFieldGroup.discard();
        listener.discardCalendarEvent();
        listener.getUI().removeWindow(scheduleEventPopup);
      }
    });

    HorizontalLayout buttons = new HorizontalLayout();
    buttons.addStyleName("v-window-bottom-toolbar");
    buttons.setWidth("100%");
    buttons.setSpacing(true);
    buttons.addComponent(deleteEventButton);
    buttons.addComponent(applyEventButton);
    buttons.setExpandRatio(applyEventButton, 1);
    buttons.setComponentAlignment(applyEventButton, Alignment.TOP_RIGHT);
    buttons.addComponent(cancel);
    layout.addComponent(buttons);
  }

  private void updateCalendarEventPopup(boolean newEvent, boolean readOnly) {
    if (scheduleEventPopup == null) {
      createCalendarEventPopup();
    }

    if (newEvent) {
      scheduleEventPopup.setCaption("New event");
    } else {
      scheduleEventPopup.setCaption("Edit event");
    }

    deleteEventButton.setVisible(!newEvent);
    deleteEventButton.setEnabled(!readOnly);
    applyEventButton.setEnabled(!readOnly);
  }

  private void updateCalendarEventForm(CalendarEvent event) {
    BeanItem<CalendarEvent> item = new BeanItem<>(event);
    scheduleEventFieldLayout.removeAllComponents();
    scheduleEventFieldGroup = new FieldGroup();
    initFormFields(scheduleEventFieldLayout, event.getClass());
    scheduleEventFieldGroup.setBuffered(true);
    scheduleEventFieldGroup.setItemDataSource(item);
  }

  public void showEventPopup(CalendarEvent event, boolean newEvent, boolean readOnly) {
    if (event == null) {
      return;
    }

    updateCalendarEventPopup(newEvent, readOnly);
    updateCalendarEventForm(event);

    if (!listener.getUI().getWindows().contains(scheduleEventPopup)) {
      listener.getUI().addWindow(scheduleEventPopup);
    }

  }

  public net.gtidev.test.model.Event getEvent() {
    BeanItem<net.gtidev.test.model.Event> item = (BeanItem<net.gtidev.test.model.Event>) scheduleEventFieldGroup.getItemDataSource();
    return item.getBean();
  }

}
