package com.event.booking.model;

import com.event.booking.enums.Category;
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
@Table(name = "tbl_event")
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String date;

    private Integer availableAttendeesCount;

    private String description;

    private Integer totalReservationCount;

    @Enumerated(EnumType.STRING)
    private Category category;

    private Date createdAt;
}
