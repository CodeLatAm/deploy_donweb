package com.abel.sistema_gestion.security.config;

import com.abel.sistema_gestion.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return  http
                .csrf( csrf -> csrf.disable())
                .authorizeHttpRequests(authRequest ->
                        authRequest

                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/vendors/create").hasAnyAuthority("PROPIETARIO")
                                .requestMatchers("/vendors/all/{userId}").permitAll()
                                .requestMatchers("/vendors/update").permitAll()
                                .requestMatchers(HttpMethod.GET,"/vendors/{userId}/{id}").permitAll()
                                .requestMatchers("/client/**").permitAll()
                                //.requestMatchers("/client/update/{id}").hasAnyAuthority("VENDEDOR")
                                .requestMatchers("/mercado-pago/notify/payment").permitAll()
                                .requestMatchers("/articles/create").hasAnyAuthority("PROPIETARIO")
                                .requestMatchers(HttpMethod.GET,"/VendorSales/sales-area/vendor/{vendorId}").hasAnyAuthority("VENDEDOR")
                                .requestMatchers(HttpMethod.GET,"/vendorSales/sales-count-by-year/{vendorId}").hasAnyAuthority("VENDEDOR")
                                .requestMatchers("/image-email.jpg", "/static/**").permitAll()  // Permitir acceso a la imagen y los archivos estáticos
                                .anyRequest().authenticated()

                )
                .sessionManagement(sessionManager ->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
