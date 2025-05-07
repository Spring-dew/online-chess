package com.chess.online.services;

import com.chess.online.entities.User;
import com.chess.online.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> allUsers() {
    List<User> users = new ArrayList<>();

    userRepository.findAll().forEach(users::add);

    return users;
  }
}
