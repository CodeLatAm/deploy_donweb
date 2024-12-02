package com.abel.sistema_gestion.repository;

import com.abel.sistema_gestion.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
   

    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    List<User> findByIsPremium(Boolean isPremium);

    @Query("SELECT u FROM User u ORDER BY u.name ASC")
    List<User> findAllOrderByName();

    Optional<User> findByUsernameAndVerificationToken(String username, boolean b);

    boolean existsByUsernameAndVerificationToken(String username, boolean b);
}
