package net.gtidev.test.model;

import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.EditableCalendarEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Entity
@Getter
@ToString
public class Event implements EditableCalendarEvent, CalendarEvent.EventChangeNotifier {

  @Id
  @Setter
  @GeneratedValue
  private Long id;

  private String caption;
  private String description;
  private Date end;
  private Date start;
  private boolean allDay;
  @ManyToOne
  private Project project;

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
    log.error("method 'setSytle' is not implemented");
  }

  @Override
  public void setAllDay(boolean allDay) {
    this.allDay = allDay;
    fireEventChange();
  }

  public void setProject(Project project) {
    this.project = project;
    fireEventChange();
  }

  @Override
  public String getStyleName() {
    return project == null ? "color1" : project.getColor();
  }
}
