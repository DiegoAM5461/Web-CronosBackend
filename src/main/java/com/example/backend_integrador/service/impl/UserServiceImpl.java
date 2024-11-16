package com.example.backend_integrador.service.impl;

import com.example.backend_integrador.dto.UserDto;
import com.example.backend_integrador.entity.Employee;
import com.example.backend_integrador.entity.User;
import com.example.backend_integrador.enums.UserEstado;
import com.example.backend_integrador.mapper.UserMapper;
import com.example.backend_integrador.repository.EmployeeRepository;
import com.example.backend_integrador.repository.UserRepository;
import com.example.backend_integrador.service.UserService;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyecta PasswordEncoder para encriptar contraseñas

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        
        UserDto userDto = UserMapper.mapToUserDto(user);
        userDto.setEmployeeId(user.getEmployee().getEmployeeId()); // Asigna el employeeId
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserDto userDto = UserMapper.mapToUserDto(user);
                    userDto.setEmployeeId(user.getEmployee().getEmployeeId()); // Asigna el employeeId
                    return userDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        // Buscar el Employee asociado
        Employee employee = employeeRepository.findById(userDto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + userDto.getEmployeeId()));

        // Crear y asignar el User al Employee
        User user = new User();
        user.setUsername(userDto.getUsername());
        
        // Encripta la contraseña antes de guardarla
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encryptedPassword);

        user.setUserEstado(userDto.getUserEstado());
        user.setUserRol(userDto.getUserRol());
        user.setEmployee(employee);

        // Guarda el User en la base de datos
        User savedUser = userRepository.save(user);

        // Convierte a DTO y devuelve
        UserDto savedUserDto = UserMapper.mapToUserDto(savedUser);
        savedUserDto.setEmployeeId(employee.getEmployeeId()); // Asigna el employeeId en el DTO
        return savedUserDto;
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        existingUser.setUsername(userDto.getUsername());

        // Encripta la nueva contraseña solo si ha sido proporcionada en userDto
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
            existingUser.setPassword(encryptedPassword);
        }

        existingUser.setUserEstado(userDto.getUserEstado());
        existingUser.setUserRol(userDto.getUserRol());

        User updatedUser = userRepository.save(existingUser);
        UserDto updatedUserDto = UserMapper.mapToUserDto(updatedUser);
        updatedUserDto.setEmployeeId(existingUser.getEmployee().getEmployeeId()); // Asigna el employeeId
        return updatedUserDto;
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        // Cambia el estado del usuario a INACTIVO en lugar de eliminarlo
        user.setUserEstado(UserEstado.INACTIVO);
        userRepository.save(user);
    }

    // Método adicional para guardar usuarios desde la interfaz o servicio
    public void saveUser(String username, String rawPassword, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        String encryptedPassword = passwordEncoder.encode(rawPassword);
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setEmployee(employee);
        user.setUserEstado(UserEstado.ACTIVO); // Asigna un estado por defecto si es necesario

        userRepository.save(user);
    }
}
