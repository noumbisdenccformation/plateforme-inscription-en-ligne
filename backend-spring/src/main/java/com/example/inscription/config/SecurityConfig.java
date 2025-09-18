package com.example.inscription.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.crypto.SecretKey;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${app.jwt.secret:dev_secret}")
	private String jwtSecret;

	@Value("${app.frontend.url:http://localhost:4200}")
	private String frontendUrl;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/admin/**").authenticated()
				.anyRequest().permitAll()
			)
			.oauth2Login(oauth -> oauth
				.userInfoEndpoint(userInfo -> userInfo.oidcUserService(new OidcUserService()))
				.successHandler(oauth2SuccessHandler())
			)
			.logout(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	public AuthenticationSuccessHandler oauth2SuccessHandler() {
		return new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
				Object principal = authentication.getPrincipal();
				String email = "";
				String sub = "";
				if (principal instanceof OidcUser oidcUser) {
					email = oidcUser.getEmail();
					sub = oidcUser.getSubject();
				}
				SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
				String token = Jwts.builder()
					.setSubject(sub)
					.claim("email", email)
					.claim("role", "USER")
					.setIssuedAt(new Date())
					.setExpiration(Date.from(Instant.now().plusSeconds(86400)))
					.signWith(key, SignatureAlgorithm.HS256)
					.compact();
				String redirect = frontendUrl + "/login?token=" + token;
				response.sendRedirect(redirect);
			}
		};
	}
} 