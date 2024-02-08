package com.event.booking;

import com.event.booking.dto.request.EventRequestDTO;
import com.event.booking.dto.request.TicketRequest;
import com.event.booking.dto.response.CreateEventResponse;
import com.event.booking.dto.response.ReserveTicketResponse;
import com.event.booking.enums.Category;
import com.event.booking.exception.AttendeesCountExceededException;
import com.event.booking.model.Event;
import com.event.booking.model.Ticket;
import com.event.booking.repository.EventRepository;
import com.event.booking.repository.TicketRepository;
import com.event.booking.service.impl.EventServiceImpl;
import com.event.booking.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.access.AccessDeniedException;

import java.util.Date;
import java.util.Optional;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingApplicationTests {
    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private UserUtil userUtil;

    @SpyBean
    private EventServiceImpl eventService;

    @BeforeAll
    public void init() {

    }

    @Test
    public void testSuccessfulEventCreation() {
        EventRequestDTO request = EventRequestDTO.builder()
                .name("livespot")
                .date("2024-02-06")
                .availableAttendeesCount(1000)
                .category(Category.Concert)
                .description("For all")
                .build();

        Event event = Event.builder()
                .id(1)
                .name("livespot")
                .date("2024-02-06")
                .availableAttendeesCount(1000)
                .category(Category.Concert)
                .description("For all")
                .build();

        Mockito.when(eventRepository.save(Mockito.any())).thenReturn(event);

        CreateEventResponse response = eventService.createEvent(request);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getEventId());
    }


    @Test
    public void testSuccessfulTicketReservation() {
        String currentUser = "aniyamoshood02@gmail.com";

        TicketRequest ticketRequest = TicketRequest.builder()
                .attendeesCount(1)
                .build();

        Event event = Event.builder()
                .id(1)
                .name("Livespot")
                .category(Category.Concert)
                .availableAttendeesCount(100)
                .description("For all")
                .build();

        Ticket ticket = Ticket.builder()
                .eventId(1)
                .status("Active")
                .reservationDate(new Date())
                .userEmail("aniyamoshood02@gmail.com")
                .sentReminder(false)
                .id(2)
                .build();

        Mockito.when(userUtil.getCurrentUserEmail()).thenReturn(currentUser);

        Mockito.when(ticketRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(ticket));

        Mockito.when(ticketRepository.save(Mockito.any())).thenReturn(ticket);

        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(event));

        ReserveTicketResponse response = eventService.reserveTicket(1, ticketRequest);

        Assert.assertNotNull(response);

        Assert.assertNotNull(response.getTicketId());
    }


    @Test
    public void testEventAttendeeCountExceeded() {
        String currentUser = "aniyamoshood02@gmail.com";

        TicketRequest ticketRequest = TicketRequest.builder()
                .attendeesCount(4)
                .build();

        Event event = Event.builder()
                .id(1)
                .name("Livespot")
                .category(Category.Concert)
                .availableAttendeesCount(2)
                .description("For all")
                .build();

        Ticket ticket = Ticket.builder()
                .eventId(4)
                .attendeesCount(4)
                .status("Active")
                .reservationDate(new Date())
                .userEmail("aniyamoshood02@gmail.com")
                .sentReminder(false)
                .id(2)
                .build();

        Mockito.when(userUtil.getCurrentUserEmail()).thenReturn(currentUser);

        Mockito.when(ticketRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(ticket));

        Mockito.when(ticketRepository.save(Mockito.any())).thenReturn(ticket);

        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(event));


        AttendeesCountExceededException attendeesCountExceededException = Assertions.assertThrows(AttendeesCountExceededException.class, () -> eventService.reserveTicket(4, ticketRequest));

        Assert.assertEquals(attendeesCountExceededException.getMessage(), "Event fully booked , check back to see if someone cancels");
    }

    @Test
    public void testSuccessfulEventTicketCancellation() {

        String currentUser = "aniyamoshood02@gmail.com";

        TicketRequest ticketRequest = TicketRequest.builder()
                .attendeesCount(1)
                .build();

        Event event = Event.builder()
                .id(1)
                .name("Livespot")
                .category(Category.Concert)
                .availableAttendeesCount(2)
                .description("For all")
                .build();

        Ticket ticket = Ticket.builder()
                .eventId(4)
                .attendeesCount(1)
                .status("Active")
                .reservationDate(new Date())
                .userEmail("aniyamoshood02@gmail.com")
                .sentReminder(false)
                .id(1)
                .build();


        Mockito.when(userUtil.getCurrentUserEmail()).thenReturn(currentUser);

        Mockito.when(ticketRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(ticket));

        Mockito.when(ticketRepository.save(Mockito.any())).thenReturn(ticket);

        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(event));

        Ticket cancelledTicket = eventService.cancelTicket(1);

        Assert.assertNotNull(cancelledTicket);
    }


    @Test
    public void testUnAuthorizedEventTicketCancellation() {

        String currentUser = "a.b@gmail.com";

        TicketRequest ticketRequest = TicketRequest.builder()
                .attendeesCount(1)
                .build();

        Event event = Event.builder()
                .id(1)
                .name("Livespot")
                .category(Category.Concert)
                .availableAttendeesCount(2)
                .description("For all")
                .build();

        Ticket ticket = Ticket.builder()
                .eventId(4)
                .attendeesCount(1)
                .status("Active")
                .reservationDate(new Date())
                .userEmail("aniyamoshood02@gmail.com")
                .sentReminder(false)
                .id(1)
                .build();


        Mockito.when(userUtil.getCurrentUserEmail()).thenReturn(currentUser);

        Mockito.when(ticketRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(ticket));

        Mockito.when(ticketRepository.save(Mockito.any())).thenReturn(ticket);

        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(event));

        AccessDeniedException accessDeniedException = Assertions.assertThrows(AccessDeniedException.class, () -> eventService.cancelTicket(1));

        Assert.assertEquals(accessDeniedException.getMessage(), "You are not authorized to perform this operation");
    }


}
