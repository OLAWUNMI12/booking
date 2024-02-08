package com.event.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_ticket")
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer eventId;

    private Integer attendeesCount;

    private String status;

    private String userEmail;

    private boolean sentReminder;

    private Date reservationDate;

    private Date createdAt;

    private Date cancelledDate;

}
