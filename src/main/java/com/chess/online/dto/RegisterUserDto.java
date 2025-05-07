package com.chess.online.dto;

import lombok.Getter;
import lombok.Setter;


public class RegisterUserDto {
  private String email;

  private String password;

  private String userName;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String username) {
    this.userName = username;
  }
}
