package com.event.booking.service;

import com.event.booking.dto.request.Credentials;
import com.event.booking.dto.request.UserRequest;
import com.event.booking.dto.response.AuthenticationResponse;
import com.event.booking.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    AuthenticationResponse register(UserRequest request) throws EntityNotFoundException;

    AuthenticationResponse authenticate(Credentials request);
}
