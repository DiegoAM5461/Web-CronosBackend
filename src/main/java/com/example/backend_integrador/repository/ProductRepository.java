package com.example.backend_integrador.repository;

import com.example.backend_integrador.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
