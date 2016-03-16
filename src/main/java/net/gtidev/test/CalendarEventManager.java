package net.gtidev.test;

import com.vaadin.ui.components.calendar.event.BasicEventProvider;
import net.gtidev.test.dbaccess.EventRepository;
import net.gtidev.test.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class CalendarEventManager {

  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private BasicEventProvider eventProvider;

  @Autowired
  private GregorianCalendar calendar;

  @PostConstruct
  public void init() {
    List<Event> all = eventRepository.findAll();
    for (Event e : all) {
      eventProvider.addEvent(e);
    }
  }
}
