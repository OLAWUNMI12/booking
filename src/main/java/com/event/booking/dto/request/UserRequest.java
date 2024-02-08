package com.event.booking.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    @NotNull(message = "Name can not be blank")
    @Size(max = 100, message = "Name characters  should be not be greater than 100")
    private String name;

    @NotNull(message = "Email can not be blank")
    @Email(message = "Email must be in a valid format")
    private String email;

    @NotNull(message = "Email can not be blank")
    @Size(min = 8, message = "password characters  should be  greater than 8")
    private String password;
}
