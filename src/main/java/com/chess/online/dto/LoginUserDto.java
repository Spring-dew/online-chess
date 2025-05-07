package com.chess.online.dto;

import lombok.Getter;
import lombok.Setter;

public class LoginUserDto {
  private String userName;
  private String password;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String username) {
    this.userName = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
