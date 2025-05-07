package com.chess.online.configurations;

import com.chess.online.configurations.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final AuthenticationProvider authenticationProvider;
  private final JwtFilter jwtAuthenticationFilter;

  public SecurityConfig(
    JwtFilter jwtAuthenticationFilter,
    AuthenticationProvider authenticationProvider
  ) {
    this.authenticationProvider = authenticationProvider;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
    // set the name of the attribute the CsrfToken will be populated on
    delegate.setCsrfRequestAttributeName("_csrf");
    // Use only the handle() method of XorCsrfTokenRequestAttributeHandler and the
    // default implementation of resolveCsrfTokenValue() from CsrfTokenRequestHandler
    CsrfTokenRequestHandler requestHandler = delegate::handle;
    http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests((requests) -> requests
//          .requestMatchers("/h2-console/**").permitAll()
          .requestMatchers("/", "/api/auth/*", "/api/auth/userlogin", "/api/auth/signup").permitAll()
          .requestMatchers("/api/game/*").permitAll()
          .requestMatchers("*.html", "*.ico", "*.js", "*.css").permitAll()
          .requestMatchers("/online-chess-game/**").permitAll()
        .anyRequest().denyAll()
//            .authenticated()
      ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authenticationProvider(authenticationProvider)
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//          .formLogin(form -> form.loginPage("/login").permitAll())
//                .httpBasic(withDefaults())
      .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
      .logout(logout -> logout.logoutUrl("/api/auth/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID", "XSRF-TOKEN"))
//      .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
//                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//      .csrf((csrf) -> csrf.csrfTokenRepository(tokenRepository).csrfTokenRequestHandler(requestHandler))
//    http.headers().frameOptions().disable();
    ;

    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().requestMatchers("/h2-console/**");
  }

}
