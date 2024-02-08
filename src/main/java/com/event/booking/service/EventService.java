package com.event.booking.service;

import com.event.booking.dto.request.EventRequestDTO;
import com.event.booking.dto.request.EventSearchRequest;
import com.event.booking.dto.request.TicketRequest;
import com.event.booking.dto.response.CreateEventResponse;
import com.event.booking.dto.response.EventResponseDTO;
import com.event.booking.dto.response.ReserveTicketResponse;
import com.event.booking.dto.response.TicketResponse;
import com.event.booking.exception.AttendeesCountExceededException;
import com.event.booking.exception.EntityNotFoundException;
import com.event.booking.model.Event;
import com.event.booking.model.Ticket;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {

    CreateEventResponse createEvent(EventRequestDTO request);

    List<EventResponseDTO> filterEvent(EventSearchRequest request);

    Event findEventById(Integer eventId);

    Ticket findTicketById(Integer ticketId);

    ReserveTicketResponse reserveTicket(Integer eventId, TicketRequest request) throws EntityNotFoundException, AttendeesCountExceededException;

    Ticket cancelTicket(Integer ticketId) throws EntityNotFoundException;

    TicketResponse viewTicket(Integer ticketId) throws EntityNotFoundException;


}
