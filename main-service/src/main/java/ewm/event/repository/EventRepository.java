package ewm.event.repository;

import ewm.event.model.Event;
import ewm.event.model.EventState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
            SELECT e FROM Event e
            WHERE (e.title ILIKE ?1
            OR e.description ILIKE ?1)
            AND e.category IN ?2
            AND e.paid = ?3
            AND e.eventDate BETWEEN ?4 AND ?5
            AND e.participantLimit = ?6
            """)
    List<Event> findEvents(String text,
                           List<Long> categories,
                           Boolean paid,
                           LocalDateTime rangeStart,
                           LocalDateTime rangeEnd,
                           Boolean onlyAvailable,
                           Pageable pageable);
}