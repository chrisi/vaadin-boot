package net.gtidev.test;

import com.vaadin.ui.components.calendar.event.CalendarEditableEventProvider;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;
import net.gtidev.test.dbaccess.EventRepository;
import net.gtidev.test.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

  @Override
  public List<CalendarEvent> getEvents(Date startDate, Date endDate) {
    List<Event> events = eventRepository.findByStartBetweenAndEndBetween(startDate, endDate, startDate, endDate);
    for (Event e : events) {
      e.addEventChangeListener(this);
    }
    List<CalendarEvent> calEvents = new ArrayList<>();
    calEvents.addAll(events);
    return calEvents;
  }

  public boolean containsEvent(Event event) {
    if (event.getId() == null)
      return false;
    return eventRepository.findOne(event.getId()) != null;
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
    Event e = (Event) changeEvent.getCalendarEvent();
    eventRepository.save(e);
    // naive implementation
    fireEventSetChange();
  }

  @Override
  public void addEvent(CalendarEvent event) {
    if (event instanceof Event) {
      Event e = (Event) event;
      Event save = eventRepository.save(e);
      e.setId(save.getId());
      e.addEventChangeListener(this);
    }
    fireEventSetChange();
  }

  @Override
  public void removeEvent(CalendarEvent event) {
    if (event instanceof Event) {
      Event e = (Event) event;
      eventRepository.delete(e);
      e.removeEventChangeListener(this);
    }
    fireEventSetChange();
  }
}
