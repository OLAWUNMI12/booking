package com.event.booking.dto.request;


import com.event.booking.enums.Category;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventSearchRequest {

    private String name;

    @Pattern(regexp = "\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2][0-9]|3[0-1])", message = "start date format must be yyyy-MM-dd")
    private String startDate;

    @Pattern(regexp = "\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2][0-9]|3[0-1])", message = "end date format must be yyyy-MM-dd")
    private String endDate;

    private Category category;

}
