package com.example.backend_integrador.entity;

import com.example.backend_integrador.enums.UserEstado;
import com.example.backend_integrador.enums.UserRol;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "userEstado")
    private UserEstado userEstado;

    @Column(name = "userRol")
    private UserRol userRol;

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", unique = true)
    private Employee employee;

}
