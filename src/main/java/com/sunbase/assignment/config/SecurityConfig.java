package com.sunbase.assignment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sunbase.assignment.security.JwtAuthentication;
import com.sunbase.assignment.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	JwtAuthentication jwtAuthentication;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/auth/**").permitAll()
		.antMatchers("/customer/**").authenticated().and().exceptionHandling()
				.authenticationEntryPoint(jwtAuthentication).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		This filter likely performs JWT validation and sets up authentication based on JWT tokens.
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
