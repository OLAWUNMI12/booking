package com.event.booking.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketRequest {

    @NotNull(message = "attendeesCount  cannot be empty")
    @Min(value = 1, message = "Attendees count should be greater than 0")
    private Integer attendeesCount;
}
