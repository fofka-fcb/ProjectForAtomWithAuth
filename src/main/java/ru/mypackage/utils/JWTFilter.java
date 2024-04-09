package ru.mypackage.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import ru.mypackage.exceptions.TokenExpiredException;
import ru.mypackage.repository.TokenRepository;
import ru.mypackage.services.auth.TokenService;


import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final TokenRepository tokenRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    public JWTFilter(TokenService tokenService, TokenRepository tokenRepository,
                     HandlerExceptionResolver handlerExceptionResolver) {
        this.tokenService = tokenService;
        this.tokenRepository = tokenRepository;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().contains("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

                String token = authorizationHeader.substring(7);
                Boolean validToken = tokenRepository.findByToken(token).map(t -> !t.getExpired()).orElse(false);

                if (validToken) {
                    SecurityContextHolder.getContext().setAuthentication(tokenService.validateToken(token));
                } else {
                    SecurityContextHolder.clearContext();
                    throw new TokenExpiredException("Token expired.", HttpStatus.UNAUTHORIZED);
                }

            }
        } catch (TokenExpiredException e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }

        filterChain.doFilter(request, response);
    }
}
