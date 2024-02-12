package com.daniel.authenticationapi.infra.filter;

import com.daniel.authenticationapi.infra.services.JWTService;
import com.daniel.authenticationapi.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService service;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("authorization");
        if (header != null) {
            String tokenSubject = service.getSubject(hasBearer(header));
            if (tokenSubject != null) {
                var user = userService.find(tokenSubject);
                if (user != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    public String hasBearer(String string) {
        return (string.startsWith("Bearer ") ? string.replace("Bearer ", "") : string);
    }
}
