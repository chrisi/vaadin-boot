package net.gtidev.test.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.calendar.event.BasicEvent;

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

// Add a two-hour event
    GregorianCalendar eventStart = new GregorianCalendar(2014, 0, 7, 13, 00, 00);
    GregorianCalendar eventEnd = new GregorianCalendar(2014, 0, 7, 16, 00, 00);
    calendar.addEvent(new BasicEvent("Calendar study", "Learning how to use Vaadin Calendar",
            eventStart.getTime(), eventEnd.getTime()));

// A two-day event
    GregorianCalendar alldayStart = new GregorianCalendar(2014, 0, 8, 0, 00, 00);
    GregorianCalendar alldayEnd = new GregorianCalendar(2014, 0, 9, 0, 00, 00);
    BasicEvent allday = new BasicEvent("Study all day", "Study how to create all-day events",
            alldayStart.getTime(), alldayEnd.getTime());
    allday.setAllDay(true);
    calendar.addEvent(allday);

    addComponent(calendar);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {

  }
}
