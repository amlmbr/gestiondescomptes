package com.example.comptes;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.comptes.entities.Compte;
import com.example.comptes.entities.TypeCompte;
import com.example.comptes.repositories.CompteRepository;
import java.util.Date;

@SpringBootApplication
public class ComptesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ComptesApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(CompteRepository compteRepository) {
        return args -> {
            // Création et sauvegarde des comptes
            Compte compte1 = new Compte(null, Math.random() * 9000, new Date(), TypeCompte.EPARGNE);
            Compte compte2 = new Compte(null, Math.random() * 9000, new Date(), TypeCompte.COURANT);
            Compte compte3 = new Compte(null, Math.random() * 9000, new Date(), TypeCompte.EPARGNE);
            
            // Sauvegarde des comptes
            compteRepository.save(compte1);
            compteRepository.save(compte2);
            compteRepository.save(compte3);
            
            // Affichage des comptes
            System.out.println("Liste des comptes :");
            compteRepository.findAll().forEach(System.out::println);
        };
    }
}
