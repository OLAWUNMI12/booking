package com.event.booking.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private int statusCode;
    private Object message;

    public ErrorResponse(Object message) {
        super();
        this.message = message;
    }

}
