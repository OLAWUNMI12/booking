package com.event.booking.controller;


import com.event.booking.dto.request.*;
import com.event.booking.dto.response.CreateEventResponse;
import com.event.booking.dto.response.EventResponseDTO;
import com.event.booking.dto.response.ReserveTicketResponse;
import com.event.booking.dto.response.TicketResponse;
import com.event.booking.exception.AttendeesCountExceededException;
import com.event.booking.exception.EntityNotFoundException;
import com.event.booking.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventBookingController {

    private final EventService eventService;


    public EventBookingController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping()
    public ResponseEntity<CreateEventResponse> createEvent(@Valid @RequestBody EventRequestDTO request) throws ParseException {

        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request));
    }


    @GetMapping()
    public ResponseEntity<List<EventResponseDTO>> filterEvents(@RequestBody EventSearchRequest request) {
        List<EventResponseDTO> events = eventService.filterEvent(request);
        if (CollectionUtils.isEmpty(events)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    @PostMapping("/{eventId}/tickets")
    public ResponseEntity<ReserveTicketResponse> reserveTicket(@Valid @RequestBody TicketRequest request,
                                                               @PathVariable @NotNull(message = "eventId cannot be null") Integer eventId) throws EntityNotFoundException, AttendeesCountExceededException {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.reserveTicket(eventId, request));
    }

    @PostMapping("/tickets/cancel")
    public ResponseEntity<Object> cancelTicket(@Valid @RequestBody CancelTicketRequest request) throws EntityNotFoundException {
        eventService.cancelTicket(request.getTicketId());
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<TicketResponse> viewTicket(@PathVariable @NotNull(message = "ticket id cannot be null") Integer ticketId) throws EntityNotFoundException {

        return ResponseEntity.status(HttpStatus.OK).body(eventService.viewTicket(ticketId));
    }
}
