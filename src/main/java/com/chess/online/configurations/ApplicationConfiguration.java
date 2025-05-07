package com.chess.online.configurations;

import com.chess.online.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfiguration {
  private final UserRepository userRepository;

  public ApplicationConfiguration(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Bean
  UserDetailsService userDetailsService() {
    return username -> userRepository.findByName(username)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  @Bean
  BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService());
//    authProvider.setPasswordEncoder(passwordEncoder());
    authProvider.setPasswordEncoder(passwordEncoder());
    System.out.println("password for test: " + passwordEncoder().encode("testpass"));
//        authProvider.setPasswordEncoder(new  org.springframework.security.crypto.password.PasswordEncoder () {
//
//          @Override
//          public String encode(CharSequence rawPassword) {
//            return rawPassword.toString();
//          }
//
//          @Override
//          public boolean matches(CharSequence rawPassword, String encodedPassword) {
//            return rawPassword.toString().equals(encodedPassword);
//          }
//
//          @Override
//          public boolean upgradeEncoding(String encodedPassword) {
//            return PasswordEncoder.super.upgradeEncoding(encodedPassword);
//          }
//        });

    return authProvider;
  }
}
