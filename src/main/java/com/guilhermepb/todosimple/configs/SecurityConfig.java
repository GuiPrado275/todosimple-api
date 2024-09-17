package com.guilhermepb.todosimple.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  //global security
public class SecurityConfig {                       //this class if SecurityFilter

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

        http.authorizeRequests().antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .anyRequest().authenticated(); //Any access or route is authorized
        //any request of PUBLIC_MATCHERS and POST, be allowed

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
