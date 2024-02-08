package com.event.booking.controller;


import com.event.booking.dto.request.Credentials;
import com.event.booking.dto.request.UserRequest;
import com.event.booking.dto.response.AuthenticationResponse;
import com.event.booking.exception.EntityNotFoundException;
import com.event.booking.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class UserAuthenticationController {

    private final AuthenticationService authenticationService;

    public UserAuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/users")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody UserRequest request) throws EntityNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(request));
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody Credentials request) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.authenticate(request));
    }

}
