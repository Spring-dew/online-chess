package com.chess.online.controllers;

import com.chess.online.dto.LoginResponse;
import com.chess.online.dto.LoginUserDto;
import com.chess.online.dto.RegisterUserDto;
import com.chess.online.entities.User;
import com.chess.online.services.AuthenticationService;
import com.chess.online.services.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "localhost:4200")
public class LoginController {

  private final JwtService jwtService;

  private final AuthenticationService authenticationService;

  public LoginController(JwtService jwtService, AuthenticationService authenticationService) {
    this.jwtService = jwtService;
    this.authenticationService = authenticationService;
  }

  @RequestMapping("/welcome")
  public String greet() {
    return "Welcome to Online Chess Multiplayer Game developed in Spring Boot.";
  }

  @RequestMapping(value = "/userlogin")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto param) {
    User authenticatedUser = authenticationService.authenticate(param);

    String jwtToken = jwtService.generateToken(authenticatedUser);

    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setToken(jwtToken);
    loginResponse.setExpiresIn(jwtService.getExpirationTime());

    return ResponseEntity.ok(loginResponse);
  }

  @RequestMapping(value = "/signup")
  public ResponseEntity<User> signup(@RequestBody RegisterUserDto param) {
    User registeredUser = authenticationService.signup(param);
    return ResponseEntity.ok(registeredUser);
  }

//  @RequestMapping(value = "/logout")
  public void logout(HttpServletRequest request) throws ServletException {
    SecurityContextHolder.clearContext();
    request.logout();
  }
}
