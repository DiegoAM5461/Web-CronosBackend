package com.example.backend_integrador.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend_integrador.entity.SpringSc.JwtResponse;
import com.example.backend_integrador.entity.Login.LoginRequest;
import com.example.backend_integrador.entity.Login.RegisterRequest;
import com.example.backend_integrador.entity.Employee;
import com.example.backend_integrador.entity.User;
import com.example.backend_integrador.enums.UserRol;
import com.example.backend_integrador.enums.UserEstado;
import com.example.backend_integrador.repository.EmployeeRepository;
import com.example.backend_integrador.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Método de login para autenticar al usuario y devolver el JWT
    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User userDetails = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(userDetails);

        return JwtResponse.builder()
                .token(token)
                .username(userDetails.getUsername()) // Incluye el nombre de usuario en la respuesta
                .build();
    }

    // Método de registro para crear un nuevo usuario con su rol de empleado
    public JwtResponse register(RegisterRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployee().getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .userRol(UserRol.EMPLEADO) // Por defecto se establece como EMPLEADO; puede personalizarse
                .userEstado(UserEstado.ACTIVO) // Estado inicial como ACTIVO
                .employee(employee) // Asociar con el empleado existente
                .build();

        userRepository.save(user);

        String token = jwtService.getToken(user);

        return JwtResponse.builder()
                .token(token)
                .build();
    }
}
