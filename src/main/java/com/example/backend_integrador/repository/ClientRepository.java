package com.example.backend_integrador.repository;

import com.example.backend_integrador.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientRepository extends JpaRepository<Client, Long>{
}