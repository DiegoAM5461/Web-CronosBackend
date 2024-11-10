package com.example.backend_integrador.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend_integrador.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
