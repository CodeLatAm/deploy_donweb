package com.abel.sistema_gestion.security.config;

import com.abel.sistema_gestion.entity.User;
import com.abel.sistema_gestion.entity.Vendor;
import com.abel.sistema_gestion.repository.UserRepository;
import com.abel.sistema_gestion.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailService() {
        return username -> {
            // Intenta cargar desde el repositorio de Admin
            Optional<User> admin = userRepository.findByUsername(username);
            if (admin.isPresent()) {
                return admin.get(); // Retorna Admin que ya implementa UserDetails
            }

            // Intenta cargar desde el repositorio de Vendor
            Optional<Vendor> vendor = vendorRepository.findByEmail(username);
            if (vendor.isPresent()) {
                // Asegura que el Vendor tenga sus roles correctamente configurados
                Vendor vendorDetails = vendor.get();
                // Puedes agregar lógica para asignar roles adicionales aquí si es necesario
                return vendorDetails;
            }

            throw new UsernameNotFoundException("User not found: " + username);
        };
    }
}
