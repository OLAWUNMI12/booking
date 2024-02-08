package com.event.booking.service.impl;

import com.event.booking.configuration.JwtService;
import com.event.booking.dto.request.Credentials;
import com.event.booking.dto.request.UserRequest;
import com.event.booking.dto.response.AuthenticationResponse;
import com.event.booking.dto.response.UserProfile;
import com.event.booking.exception.DuplicateUserException;
import com.event.booking.exception.EntityNotFoundException;
import com.event.booking.model.User;
import com.event.booking.repository.UserRepository;
import com.event.booking.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public AuthenticationResponse register(UserRequest request) throws EntityNotFoundException {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent())
            throw new DuplicateUserException("User with email : " + request.getEmail() + " already created.");

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setCreatedAt(new Date());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        UserProfile userProfile = new UserProfile(user);
        userProfile.setToken(token);
        return new AuthenticationResponse(userProfile);
    }


    public AuthenticationResponse authenticate(Credentials request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user);
        UserProfile userProfile = new UserProfile(user);
        userProfile.setToken(token);
        return new AuthenticationResponse(userProfile);
    }


}
