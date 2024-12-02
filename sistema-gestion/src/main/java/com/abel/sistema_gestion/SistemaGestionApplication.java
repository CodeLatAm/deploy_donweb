package com.abel.sistema_gestion;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.crypto.SecretKey;

@SpringBootApplication
@EnableScheduling  // Habilita las tareas programadas
public class SistemaGestionApplication {

	public static void main(String[] args) {


		SpringApplication.run(SistemaGestionApplication.class, args);
		/*
		SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256); // Genera una clave segura
		String base64Key = java.util.Base64.getEncoder().encodeToString(key.getEncoded()); // Convertir a Base64
		System.out.println("Generated Secret Key: " + base64Key);

		*/
	}

}
