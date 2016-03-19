package net.gtidev.test.ui.view;

import com.vaadin.ui.UI;

public interface CalendarEventListener {
  void commitCalendarEvent();

  void discardCalendarEvent();

  void deleteCalendarEvent();

  UI getUI();
}
