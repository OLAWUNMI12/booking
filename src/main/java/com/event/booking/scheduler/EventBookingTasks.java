package com.event.booking.scheduler;

import com.event.booking.model.EventBookingNotificationAudit;
import com.event.booking.model.Ticket;
import com.event.booking.repository.EventBookingNotificationAuditRepository;
import com.event.booking.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Stream;

@Slf4j
@Component
public class EventBookingTasks {

    private final EventBookingNotificationAuditRepository eventBookingNotificationAuditRepository;

    private final TicketRepository ticketRepository;

    private final int dayDifferenceForReminder;

    public EventBookingTasks(EventBookingNotificationAuditRepository eventBookingNotificationAuditRepository,
                             TicketRepository ticketRepository,
                             @Value("${day.difference.for.reminder}") int dayDifferenceForReminder) {
        this.eventBookingNotificationAuditRepository = eventBookingNotificationAuditRepository;
        this.ticketRepository = ticketRepository;
        this.dayDifferenceForReminder = dayDifferenceForReminder;
    }

    @Scheduled(cron = "0 */12  * * * *") // Runs every 12 hours
    @Transactional
    public void eventNotificationTask() {
        log.info("--------Task to send event reminder notification started-----------");

        Stream<Ticket> ticketStream = ticketRepository.streamEventTicketByDateDifference(dayDifferenceForReminder);

        ticketStream.forEach(ticket -> sendEventBookingNotification(ticket));

        log.info("--------Task to send event reminder notification closed gracefully-----------");
    }


    private void sendEventBookingNotification(Ticket ticket) {

        //Send Notification to user making an assumption that there is a notification service
        // notificationService.sendEventBookingNotificationMail(notificationText, userEmail, ticketId);

        log.info("Sent notification to user: {} who booked  ticket : #{}", ticket.getUserEmail(), ticket.getId());

        EventBookingNotificationAudit eventBookingNotificationAudit = EventBookingNotificationAudit.builder()
                .eventId(ticket.getEventId())
                .userEmail(ticket.getUserEmail())
                .ticketId(ticket.getId())
                .createdAt(new Date())
                .build();

        eventBookingNotificationAuditRepository.save(eventBookingNotificationAudit);

        ticket.setSentReminder(true);
        ticketRepository.save(ticket);
    }
}
