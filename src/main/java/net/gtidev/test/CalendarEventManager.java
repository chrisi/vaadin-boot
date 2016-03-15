package net.gtidev.test;

import com.vaadin.ui.components.calendar.event.BasicEventProvider;
import net.gtidev.test.ui.view.MyCalendarEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class CalendarEventManager {

  @Autowired
  private BasicEventProvider dataSource;

  @Autowired
  private GregorianCalendar calendar;

  @PostConstruct
  public void init() {
    Date originalDate = calendar.getTime();
    Date today = new Date();

    // Add a event that last a whole week

    Date start = resolveFirstDateOfWeek(today, calendar);
    Date end = resolveLastDateOfWeek(today, calendar);
    MyCalendarEvent event = getNewEvent("Whole week event", start, end);
    event.setAllDay(true);
    event.setStyleName("color4");
    event.setDescription("Description for the whole week event.");
    dataSource.addEvent(event);

    // Add a allday event
    calendar.setTime(start);
    calendar.add(GregorianCalendar.DATE, 3);
    start = calendar.getTime();
    end = start;
    event = getNewEvent("All-day event", start, end);
    event.setAllDay(true);
    event.setDescription("Some description.");
    event.setStyleName("color3");
    dataSource.addEvent(event);

    // Add a second allday event
    calendar.add(GregorianCalendar.DATE, 1);
    start = calendar.getTime();
    end = start;
    event = getNewEvent("Second all-day event", start, end);
    event.setAllDay(true);
    event.setDescription("Some description.");
    event.setStyleName("color2");
    dataSource.addEvent(event);

    calendar.add(GregorianCalendar.DATE, -3);
    calendar.set(GregorianCalendar.HOUR_OF_DAY, 9);
    calendar.set(GregorianCalendar.MINUTE, 30);
    start = calendar.getTime();
    calendar.add(GregorianCalendar.HOUR_OF_DAY, 5);
    calendar.set(GregorianCalendar.MINUTE, 0);
    end = calendar.getTime();
    event = getNewEvent("Appointment", start, end);
    event.setWhere("Office");
    event.setStyleName("color1");
    event.setDescription("A longer description, which should display correctly.");
    dataSource.addEvent(event);

    calendar.add(GregorianCalendar.DATE, 1);
    calendar.set(GregorianCalendar.HOUR_OF_DAY, 11);
    calendar.set(GregorianCalendar.MINUTE, 0);
    start = calendar.getTime();
    calendar.add(GregorianCalendar.HOUR_OF_DAY, 8);
    end = calendar.getTime();
    event = getNewEvent("Training", start, end);
    event.setStyleName("color2");
    dataSource.addEvent(event);

    calendar.add(GregorianCalendar.DATE, 4);
    calendar.set(GregorianCalendar.HOUR_OF_DAY, 9);
    calendar.set(GregorianCalendar.MINUTE, 0);
    start = calendar.getTime();
    calendar.add(GregorianCalendar.HOUR_OF_DAY, 9);
    end = calendar.getTime();
    event = getNewEvent("Free time", start, end);
    dataSource.addEvent(event);

    calendar.setTime(originalDate);
  }

  private MyCalendarEvent getNewEvent(String caption, Date start, Date end) {
    MyCalendarEvent event = new MyCalendarEvent();
    event.setCaption(caption);
    event.setStart(start);
    event.setEnd(end);
    return event;
  }

  private Date resolveFirstDateOfWeek(Date today, java.util.Calendar currentCalendar) {
    int firstDayOfWeek = currentCalendar.getFirstDayOfWeek();
    currentCalendar.setTime(today);
    while (firstDayOfWeek != currentCalendar.get(java.util.Calendar.DAY_OF_WEEK)) {
      currentCalendar.add(java.util.Calendar.DATE, -1);
    }
    return currentCalendar.getTime();
  }

  private Date resolveLastDateOfWeek(Date today, java.util.Calendar currentCalendar) {
    currentCalendar.setTime(today);
    currentCalendar.add(java.util.Calendar.DATE, 1);
    int firstDayOfWeek = currentCalendar.getFirstDayOfWeek();
    // Roll to weeks last day using firstdayofweek. Roll until FDofW is
    // found and then roll back one day.
    while (firstDayOfWeek != currentCalendar.get(java.util.Calendar.DAY_OF_WEEK)) {
      currentCalendar.add(java.util.Calendar.DATE, 1);
    }
    currentCalendar.add(java.util.Calendar.DATE, -1);
    return currentCalendar.getTime();
  }
}
