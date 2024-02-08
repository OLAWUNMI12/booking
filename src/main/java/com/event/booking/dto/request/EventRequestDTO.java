package com.event.booking.dto.request;

import com.event.booking.enums.Category;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequestDTO {

    @NotNull(message = "Name cannot be empty")
    @Size(max = 100, message = "Name has exceeded maximum number of character (100)")
    private String name;

    @NotNull(message = "Date cannot be empty")
    @Pattern(regexp = "\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2][0-9]|3[0-1])", message = "Date format must be yyyy-MM-dd")
    private String date;

    @NotNull(message = "Available attendee count  cannot be empty")
    @Min(value = 1, message = "Available attendee count  should be greater than 0")
    @Max(value = 1000, message = "Available attendee count  should be not greater than 1000")
    private Integer availableAttendeesCount;

    @NotNull(message = "Description  cannot be empty")
    @Size(max = 500, message = "Description has exceeded maximum number of character (500)")
    private String description;

    @NotNull(message = "Category  cannot be empty")
    private Category category;

}
