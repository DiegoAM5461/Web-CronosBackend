package com.example.backend_integrador.repository;

import com.example.backend_integrador.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface ClientRepository extends JpaRepository<Client, Long>{
    Optional<Client> findByEmail(String email);

}