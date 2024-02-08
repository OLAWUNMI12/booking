package com.event.booking.service.impl;

import com.event.booking.dto.request.EventRequestDTO;
import com.event.booking.dto.request.EventSearchRequest;
import com.event.booking.dto.request.TicketRequest;
import com.event.booking.dto.response.CreateEventResponse;
import com.event.booking.dto.response.EventResponseDTO;
import com.event.booking.dto.response.ReserveTicketResponse;
import com.event.booking.dto.response.TicketResponse;
import com.event.booking.enums.Status;
import com.event.booking.exception.AttendeesCountExceededException;
import com.event.booking.exception.EntityNotFoundException;
import com.event.booking.model.Event;
import com.event.booking.model.Ticket;
import com.event.booking.repository.EventRepository;
import com.event.booking.repository.TicketRepository;
import com.event.booking.service.EventService;
import com.event.booking.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    private final UserUtil userUtil;

    public EventServiceImpl(EventRepository eventRepository, TicketRepository ticketRepository, UserUtil userUtil) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.userUtil = userUtil;
    }

    @Override
    public CreateEventResponse createEvent(EventRequestDTO request) {
        Event event = Event.builder().name(request.getName())
                .date(request.getDate())
                .category(request.getCategory())
                .availableAttendeesCount(request.getAvailableAttendeesCount())
                .description(request.getDescription())
                .createdAt(new Date())
                .build();

        event = eventRepository.save(event);
        log.info("Created new event {}", event.getName());
        return CreateEventResponse.builder().eventId(event.getId()).build();
    }

    @Override
    public List<EventResponseDTO> filterEvent(EventSearchRequest request) {
        log.info("Fetching  events... ");
        List<Event> events = eventRepository.filterEvents(request.getName(),
                request.getStartDate(),
                request.getEndDate(),
                request.getCategory());

        List<EventResponseDTO> dtos = new ArrayList<>();
        for (Event event : events) {
            dtos.add(EventResponseDTO.builder().id(event.getId())
                    .name(event.getName())
                    .date(event.getDate())
                    .availableAttendeesCount(event.getAvailableAttendeesCount())
                    .description(event.getDescription())
                    .category(event.getCategory())
                    .build()
            );
        }
        return dtos;
    }

    @Override
    public Event findEventById(Integer eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        return event.isPresent() ? event.get() : null;
    }

    @Override
    public Ticket findTicketById(Integer ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        return ticket.isPresent() ? ticket.get() : null;
    }

    @Override
    public Ticket cancelTicket(Integer ticketId) throws EntityNotFoundException {
        Ticket ticket = findTicketById(ticketId);

        if (!validateUser(ticket.getUserEmail()))
            throw new AccessDeniedException("You are not authorized to perform this operation");

        if (Objects.isNull(ticket))
            throw new EntityNotFoundException("Ticket with id : #" + ticketId + " not found ");

        ticket.setStatus(Status.Cancelled.name());
        ticket.setCancelledDate(new Date());
        ticketRepository.save(ticket);

        Event event = findEventById(ticket.getEventId());
        event.setTotalReservationCount(event.getAvailableAttendeesCount() - ticket.getAttendeesCount());
        eventRepository.save(event);
        log.info("Cancelled ticket : #{} ", ticket.getId());
        return ticket;
    }

    @Override
    public TicketResponse viewTicket(Integer ticketId) throws EntityNotFoundException {
        Ticket ticket = findTicketById(ticketId);

        if (!validateUser(ticket.getUserEmail()))
            throw new AccessDeniedException("You are not authorized to perform this operation");

        if (Objects.isNull(ticket))
            throw new EntityNotFoundException("Ticket with id : #" + ticketId + " not found ");

        return TicketResponse.builder().id(ticket.getId())
                .eventId(ticket.getEventId())
                .attendeesCount(ticket.getAttendeesCount())
                .build();
    }

    @Override
    public ReserveTicketResponse reserveTicket(Integer eventId, TicketRequest request) throws EntityNotFoundException, AttendeesCountExceededException {
        Event event = findEventById(eventId);

        if (Objects.isNull(event))
            throw new EntityNotFoundException("Event with id : #" + eventId + " not found ");

        int totalAttendee = (Objects.isNull(event.getTotalReservationCount()) ? 0 : event.getTotalReservationCount()) + request.getAttendeesCount();

        if (totalAttendee > event.getAvailableAttendeesCount())
            throw new AttendeesCountExceededException("Event fully booked , check back to see if someone cancels");

        Ticket ticket = Ticket.builder().eventId(eventId)
                .attendeesCount(request.getAttendeesCount())
                .status(Status.Active.name())
                .userEmail(userUtil.getCurrentUserEmail())
                .reservationDate(new Date())
                .sentReminder(false)
                .createdAt(new Date())
                .build();
        ticket = ticketRepository.save(ticket);

        event.setTotalReservationCount(totalAttendee);
        eventRepository.save(event);

        log.info("New ticket: #{} reserved for event: {} ", ticket.getId(), event.getName());
        return ReserveTicketResponse.builder().ticketId(ticket.getId()).build();
    }

    public boolean validateUser(String userName) {
        return userName.equalsIgnoreCase(userUtil.getCurrentUserEmail());
    }


}
