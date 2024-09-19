package com.guilhermepb.todosimple.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JWTUtil jwtUtil;

    private UserDetailsService userDetailsService;

    //constructor
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager
            , JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override //super function
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String authorizationHeader = request.getHeader("Authorization");
        if (Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);//if Objects is not null and it starts with Bearer
            UsernamePasswordAuthenticationToken auth = getAuthentication(token);//but Bearer isn't part of token
            if (Objects.nonNull(auth)) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(request, response);
        }

    }

    //for authenticate token, if token is valid:
    //takes the token data to seek authentication using the token itself, receiving the token checks if it is valid and
    // extracts the username to search for the user, thus returning what the internal filter needs
    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if(this.jwtUtil.isValidToken(token)) {
            String username = this.jwtUtil.getUsername(token);
            UserDetails user = this.userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticatedUser = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
            return authenticatedUser;
        } else {
            return null;
        }
    }

}
