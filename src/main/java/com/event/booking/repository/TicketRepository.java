package com.event.booking.repository;

import com.event.booking.model.Ticket;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Query("SELECT p FROM Ticket p  "
            + "LEFT JOIN Event e ON e.id = p.eventId"
            + " WHERE DATEDIFF(DAY, p.reservationDate,  CAST(e.date AS DATE)) = :dayDifferenceForReminder"
            + " AND p.sentReminder = FALSE AND p.status <> 'Cancelled' "
    )
    @QueryHints(
            @QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "20")
    )
    Stream<Ticket> streamEventTicketByDateDifference(@Param("dayDifferenceForReminder") Integer dayDifferenceForReminder);
}
