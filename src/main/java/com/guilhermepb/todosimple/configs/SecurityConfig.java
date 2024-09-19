package com.guilhermepb.todosimple.configs;

import com.guilhermepb.todosimple.security.JWTAuthenticationFilter;
import com.guilhermepb.todosimple.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  //global security
public class SecurityConfig {//this class if SecurityFilter

    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

    private static final String[] PUBLIC_MATCHERS = { //libre route
            "/"
    };

    private static final String[] PUBLIC_MATCHERS_POST = {  //user and login is public for post
            "/user",
            "/login"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{//filterChain receives the http request

        http.cors().and().csrf().disable(); //disable the csrf protection

        AuthenticationManagerBuilder authenticationManagerBuilder = http //add to builder for authentication manager
                .getSharedObject(AuthenticationManagerBuilder.class);    //with encrypt on
        authenticationManagerBuilder.userDetailsService(this.userDetailsService)
        .passwordEncoder(new BCryptPasswordEncoder());
        this.authenticationManager = authenticationManagerBuilder.build();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .anyRequest().authenticated().and()            //Any access or route is authorized
                .authenticationManager(authenticationManager);  //Any request of PUBLIC_MATCHERS and POST, be allowed

        http.addFilter(new JWTAuthenticationFilter(this.authenticationManager, this.jwtUtil)); //authenticator filter

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //the session can't save

        return http.build(); //constructor
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues(); //cors configuration
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE")); //allow methods
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() { //to encrypt
        return new BCryptPasswordEncoder();
    }

}
