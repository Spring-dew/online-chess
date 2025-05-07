package com.chess.online.configurations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "RequestCachingFilter", urlPatterns = "/*")
public class RequestFilter extends OncePerRequestFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info("Request DATA: " + request.getMethod() + " ");
//        Stream.of(request.getHeaderNames().asIterator()).forEach(e -> System.out.println(e + " " + request.getHeader(String.valueOf(e))));
      Enumeration<String> headerNames = request.getHeaderNames();

      if (headerNames != null) {
        while (headerNames.hasMoreElements()) {
          String s = headerNames.nextElement();
//          System.out.println("Header: " + s + " " + request.getHeader(s));
        }
      }
        filterChain.doFilter(request, response);
    }
}
