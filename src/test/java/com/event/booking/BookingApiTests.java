package com.event.booking;


import com.event.booking.controller.EventBookingController;
import com.event.booking.dto.request.CancelTicketRequest;
import com.event.booking.dto.request.Credentials;
import com.event.booking.dto.request.EventRequestDTO;
import com.event.booking.dto.request.TicketRequest;
import com.event.booking.dto.response.AuthenticationResponse;
import com.event.booking.enums.Category;
import com.event.booking.model.Event;
import com.event.booking.model.Ticket;
import com.event.booking.repository.EventRepository;
import com.event.booking.repository.TicketRepository;
import com.event.booking.service.impl.EventServiceImpl;
import com.event.booking.util.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingApiTests {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FilterChainProxy filterChainProxy;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private UserUtil userUtil;


    @SpyBean
    private EventServiceImpl eventService;

    @SpyBean
    private EventBookingController eventBookingController;


    @BeforeAll
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(filterChainProxy).build();
    }


    public String generateToken() throws Exception {
        Credentials request = Credentials.builder()
                .email("aniyamoshood02@gmail.com")
                .password("12345678")
                .build();

        String body = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(
                        post("/auth")
                                .content(body)
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                ).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AuthenticationResponse authenticationResponse = objectMapper.readValue(response, AuthenticationResponse.class);
        System.out.println(authenticationResponse.getUserProfile().getToken());

        return authenticationResponse.getUserProfile().getToken();

    }

    @Test
    public void testSuccessfulEventCreationEndpoint() throws Exception {
        EventRequestDTO request = EventRequestDTO.builder()
                .name("livespot")
                .date("2024-02-06")
                .availableAttendeesCount(1000)
                .category(Category.Concert)
                .description("For all").build();

        Event event = Event.builder()
                .id(1)
                .name("livespot")
                .date("2024-02-06")
                .availableAttendeesCount(1000)
                .category(Category.Concert)
                .description("For all")
                .build();

        Mockito.when(eventRepository.save(Mockito.any())).thenReturn(event);

        String body = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(
                        post("/events")
                                .content(body)
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + generateToken())
                ).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }


    @Test
    public void testUnauthorizedEventCreationEndpoint() throws Exception {
        EventRequestDTO request = EventRequestDTO.builder()
                .name("livespot")
                .date("2024-02-06")
                .availableAttendeesCount(1000)
                .category(Category.Concert)
                .description("For all").build();

        String body = objectMapper.writeValueAsString(request);

        String response = mockMvc.perform(
                        post("/events")
                                .content(body)
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                ).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }


    @Test
    public void testSuccessfulTicketReservation() throws Exception {
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

        String body = objectMapper.writeValueAsString(ticketRequest);


        String response = mockMvc.perform(
                        post("/events/1/tickets")
                                .content(body)
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + generateToken())
                ).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }


    @Test
    public void testEventAttendeeCountExceeded() throws Exception {
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

        String body = objectMapper.writeValueAsString(ticketRequest);

        String response = mockMvc.perform(
                        post("/events/1/tickets")
                                .content(body)
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + generateToken())
                ).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }


    @Test
    public void testSuccessfulEventTicketCancellation() throws Exception {

        String currentUser = "aniyamoshood02@gmail.com";

        CancelTicketRequest cancelTicketRequest = CancelTicketRequest.builder()
                .ticketId(1)
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


        String body = objectMapper.writeValueAsString(cancelTicketRequest);

        String response = mockMvc.perform(
                        post("/events/tickets/cancel")
                                .content(body)
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + generateToken())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

    }


    @Test
    public void testUnAuthorizedEventTicketCancellation() throws Exception {

        String currentUser = "a.b@gmail.com";

        CancelTicketRequest cancelTicketRequest = CancelTicketRequest.builder()
                .ticketId(1)
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

        String body = objectMapper.writeValueAsString(cancelTicketRequest);

        String response = mockMvc.perform(
                        post("/events/tickets/cancel")
                                .content(body)
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + generateToken())
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }


}
