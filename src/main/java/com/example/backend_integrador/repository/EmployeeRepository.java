package com.example.backend_integrador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend_integrador.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
