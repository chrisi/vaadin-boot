package net.gtidev.test.model;

import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.EditableCalendarEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(of = {"id"})
public class Event implements EditableCalendarEvent, CalendarEvent.EventChangeNotifier {

  @Id
  @GeneratedValue
  private Long id;

  private String caption;
  private String description;
  private Date end;
  private Date start;
  private String styleName;
  private boolean allDay;

  private transient List<EventChangeListener> listeners = new ArrayList<>();

  public Event() {
  }

  public Event(Long id, String caption, String description, Date start, Date end, boolean allDay, String styleName) {
    this.id = id;
    this.caption = caption;
    this.description = description;
    this.start = start;
    this.end = end;
    this.allDay = allDay;
    this.styleName = styleName;
  }

  public Event(Long id, String caption, String description, Date start, double hours, String styleName) {
    this(id, caption, description, start, Date.from(start.toInstant().plus(Duration.ofMinutes((long) (60 * hours)))), false, styleName);
  }

  public Event(Long id, String caption, String description, Date date, String styleName) {
    this(id, caption, description, date, date, true, styleName);
  }

  @Override
  public void addEventChangeListener(EventChangeListener listener) {
    listeners.add(listener);
  }

  @Override
  public void removeEventChangeListener(EventChangeListener listener) {
    listeners.remove(listener);
  }

  private void fireEventChange() {
    EventChangeEvent event = new EventChangeEvent(this);
    for (EventChangeListener listener : listeners) {
      listener.eventChange(event);
    }
  }

  @Override
  public void setCaption(String caption) {
    this.caption = caption;
    fireEventChange();
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
    fireEventChange();
  }

  @Override
  public void setEnd(Date end) {
    this.end = end;
    fireEventChange();
  }

  @Override
  public void setStart(Date start) {
    this.start = start;
    fireEventChange();
  }

  @Override
  public void setStyleName(String styleName) {
    this.styleName = styleName;
    fireEventChange();
  }

  @Override
  public void setAllDay(boolean allDay) {
    this.allDay = allDay;
    fireEventChange();
  }
}