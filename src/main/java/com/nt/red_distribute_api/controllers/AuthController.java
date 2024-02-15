package com.nt.red_distribute_api.controllers;

import com.nt.red_distribute_api.Auth.JwtHelper;
import com.nt.red_distribute_api.config.AuthConfig;
import com.nt.red_distribute_api.dto.req.JwtRequest;
import com.nt.red_distribute_api.dto.req.UserRequestDto;
import com.nt.red_distribute_api.dto.resp.AuthSuccessResp;
import com.nt.red_distribute_api.dto.resp.JwtErrorResp;
import com.nt.red_distribute_api.dto.resp.LoginResp;
import com.nt.red_distribute_api.exp.UserAlreadyExistsException;
import com.nt.red_distribute_api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthConfig authConfig;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private JwtHelper helper;


    @Autowired
    private UserService userService;


    @PostMapping("/create")
    public ResponseEntity<AuthSuccessResp> createUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            LoginResp userResponseDto = userService.createUser(userRequestDto);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userResponseDto.getEmail());
            System.out.println("from db info");
            System.out.println(userDetails.getUsername());
            System.out.println(userDetails.getPassword());

            String token = this.helper.generateToken(userDetails);
            return new ResponseEntity<>(new AuthSuccessResp(token), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException ex) {
            // Handle the exception and return an appropriate response
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new AuthSuccessResp("User already exists: " + ex.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResp> login(@RequestBody JwtRequest jwtRequest) {
        this.doAuthenticate(jwtRequest.getEmail(), jwtRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        String token = this.helper.generateToken(userDetails);
        // JwtResponse jwtResponse = JwtResponse.builder().token(token).build();
        LoginResp userResp = new LoginResp();
        userResp.setEmail(userDetails.getUsername());
        userResp.setJwtToken(token);
        System.out.println("token:"+token);

        return new ResponseEntity<>(userResp, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        System.out.println("Login Info");
        System.out.println(email);
        System.out.println(password);
        System.out.println("------");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Authentication successful for user: " + email);


        } catch (BadCredentialsException e) {
            System.out.println("Authentication not-successful for user: " + email);
            throw new BadCredentialsException(" Invalid Username or Password  !!");

        }

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<JwtErrorResp> exceptionHandler(BadCredentialsException ex) {
        return new ResponseEntity<>(new JwtErrorResp(400, "Credentials Invalid !!"), HttpStatus.BAD_REQUEST);
    }
}
