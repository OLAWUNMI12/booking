package com.event.booking.dto.response;

import com.event.booking.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventResponseDTO {

    private Integer id;
    private String name;
    private String date;
    private Integer availableAttendeesCount;
    private String description;
    private Category category;
}
