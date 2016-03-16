package net.gtidev.test;

import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.CalendarEditableEventProvider;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;
import net.gtidev.test.dbaccess.EventRepository;
import net.gtidev.test.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class EventProvider implements CalendarEditableEventProvider,
        CalendarEventProvider.EventSetChangeNotifier, CalendarEvent.EventChangeListener {

  @Autowired
  private EventRepository eventRepository;

  private List<CalendarEvent> eventList = new ArrayList<>();

  private List<EventSetChangeListener> listeners = new ArrayList<>();

  @PostConstruct
  public void init() {
    List<Event> all = eventRepository.findAll();
    all.forEach(this::addEvent);
  }

  @Override
  public List<CalendarEvent> getEvents(Date startDate, Date endDate) {
    ArrayList<CalendarEvent> activeEvents = new ArrayList<>();

    for (CalendarEvent ev : eventList) {
      long from = startDate.getTime();
      long to = endDate.getTime();

      if (ev.getStart() != null && ev.getEnd() != null) {
        long f = ev.getStart().getTime();
        long t = ev.getEnd().getTime();
        // Select only events that overlaps with startDate and endDate.
        if ((f <= to && f >= from) || (t >= from && t <= to)
                || (f <= from && t >= to)) {
          activeEvents.add(ev);
        }
      }
    }

    return activeEvents;
  }

  public boolean containsEvent(Event event) {
    return eventList.contains(event);
  }

  @Override
  public void addEventSetChangeListener(EventSetChangeListener listener) {
    listeners.add(listener);
  }

  @Override
  public void removeEventSetChangeListener(EventSetChangeListener listener) {
    listeners.remove(listener);
  }

  /**
   * Fires a eventsetchange event. The event is fired when either an event is
   * added or removed to the event provider
   */
  private void fireEventSetChange() {
    EventSetChangeEvent event = new EventSetChangeEvent(this);

    for (EventSetChangeListener listener : listeners) {
      listener.eventSetChange(event);
    }
  }

  @Override
  public void eventChange(CalendarEvent.EventChangeEvent changeEvent) {
    // naive implementation
    fireEventSetChange();
  }

  @Override
  public void addEvent(CalendarEvent event) {
    eventList.add(event);
    if (event instanceof BasicEvent) {
      ((BasicEvent) event).addEventChangeListener(this);
    }
    fireEventSetChange();
  }

  @Override
  public void removeEvent(CalendarEvent event) {
    eventList.remove(event);
    if (event instanceof BasicEvent) {
      ((BasicEvent) event).removeEventChangeListener(this);
    }
    fireEventSetChange();
  }
}
