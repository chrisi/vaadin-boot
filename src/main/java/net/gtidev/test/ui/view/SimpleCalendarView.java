package net.gtidev.test.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;
import java.util.GregorianCalendar;
import java.util.Locale;

@SpringView(name = "simpleCal")
public class SimpleCalendarView extends VerticalLayout implements View {

  @PostConstruct
  public void init() {
    setSpacing(true);
// Create the calendar
    Calendar calendar = new Calendar("Simple Calendar");
    calendar.setWidth("1000px");  // Undefined by default
    calendar.setHeight("500px"); // Undefined by default

// Use US English for date/time representation
    calendar.setLocale(new Locale("en", "US"));

// We use a fixed date range in this example
    calendar.setStartDate(new GregorianCalendar(2014, 0, 6, 13, 00, 00).getTime());
    calendar.setEndDate(new GregorianCalendar(2014, 0, 12, 13, 00, 00).getTime());

// Set daily time range
    calendar.setFirstVisibleHourOfDay(9);
    calendar.setLastVisibleHourOfDay(17);

//TODO: Add Events

    addComponent(calendar);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {

  }
}
