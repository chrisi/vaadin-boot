package net.gtidev.test.dbaccess;

import net.gtidev.test.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
  List<Event> findByStartBetweenAndEndBetween(Date startfrom, Date startTo, Date endFrom, Date endTo);
}
