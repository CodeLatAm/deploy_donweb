package com.abel.sistema_gestion.config;

import com.abel.sistema_gestion.dto.auth.RegisterRequest;
import com.abel.sistema_gestion.entity.Category;
import com.abel.sistema_gestion.entity.User;
import com.abel.sistema_gestion.enums.Role;
import com.abel.sistema_gestion.enums.UserStatus;
import com.abel.sistema_gestion.repository.CategoryRepository;
import com.abel.sistema_gestion.repository.UserRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Seeders implements CommandLineRunner {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        var categories = List.of(
          new Category("Almacén"),
                new Category("Bebidas"),
                new Category("Frescos"),
                new Category( "Congelados"),
                new Category( "Limpieza"),
                new Category( "Perfumería"),
                new Category( "Congelados"),
                new Category("Electro"),
                new Category( "Textil"),
                new Category( "Hogar"),
                new Category( "Aire libre")
        );
        // Guardar las categorías en la base de datos si no existen
        categories.forEach(category -> {
            if (!categoryRepository.existsByName(category.getName())) {
                categoryRepository.save(category);
            }
        });

        //Crea user ADMIN
        if(!userRepository.existsByUsername("admin3581@gmail.com")){
            RegisterRequest request = this.dtoCreateAdmin();
            User user = createUserAdmin(request);
            userRepository.save(user);
        }

    }

    private User createUserAdmin(RegisterRequest request) {
        return User.builder()
                .name(request.getName())
                .role(Role.ADMIN)
                .companyName(request.getCompanyName())
                .password(request.getPassword())
                .username(request.getUsername())
                .companyName(request.getCompanyName())
                .isPremium(true)
                .premiumExpirationDate(LocalDate.of(2050,12, 1))
                .userStatus(UserStatus.PREMIUM)
                .startOfActivity(LocalDate.now())
                .verificationToken(true)
                .build();
    }

    private RegisterRequest dtoCreateAdmin(){
        return RegisterRequest.builder()
                .name("Abel")
                .companyName("Free Lance 👽")
                .lastname("Acevedo")
                .username("admin3581@gmail.com")
                .password(passwordEncoder.encode("12345678"))

                .build();
    }
}
