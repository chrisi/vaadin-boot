package net.gtidev.test.dbaccess;

import net.gtidev.test.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
