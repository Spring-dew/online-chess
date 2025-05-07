package com.chess.online;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
@CrossOrigin(origins = {"localhost:4200"})
public class Application {
	private final static Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		LOGGER.info("Welcome to Online Chess Multiplayer Game.");
		SpringApplication.run(Application.class, args);
	}

//  @Bean
  public DispatcherServlet dispatcherServlet() {
    return new DispatcherServlet();
  }

//  @Bean
//  @RequestMapping("/api")
  public ServletRegistrationBean dispatcherServletRegistration() {
    ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet());
    registration.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
    return registration;
  }

}
