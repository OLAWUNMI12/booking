package com.event.booking.repository;

import com.event.booking.model.EventBookingNotificationAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventBookingNotificationAuditRepository extends JpaRepository<EventBookingNotificationAudit, Integer> {
}
