package com.example.backend_integrador.service.impl;

import com.example.backend_integrador.dto.EmployeeDto;
import com.example.backend_integrador.entity.Employee;
import com.example.backend_integrador.enums.EmployeeEstado;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.mapper.EmployeeMapper;
import com.example.backend_integrador.repository.EmployeeRepository;
import com.example.backend_integrador.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        logger.info("Creando un nuevo empleado con email: {}", employeeDto.getEmail());

        if (employeeRepository.existsByEmail(employeeDto.getEmail())) {
            logger.error("El email {} ya está en uso", employeeDto.getEmail());
            throw new IllegalArgumentException("El email ya está en uso.");
        }

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        employee.setEstadoEmployee(EmployeeEstado.INACTIVO); // Estado por defecto

        Employee savedEmployee = employeeRepository.save(employee);

        logger.info("Empleado creado con éxito: {}", savedEmployee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        logger.info("Obteniendo empleado por ID: {}", employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    logger.error("Empleado con ID {} no encontrado", employeeId);
                    return new ResourceNotFoundException("Empleado no encontrado con ID: " + employeeId);
                });

        logger.debug("Empleado encontrado: {}", employee);
        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        logger.info("Obteniendo todos los empleados");

        List<Employee> employees = employeeRepository.findAll();

        logger.debug("Cantidad de empleados encontrados: {}", employees.size());
        return employees.stream()
                .map(EmployeeMapper::mapToEmployeeDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto) {
        logger.info("Actualizando empleado con ID: {}", employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    logger.error("Empleado con ID {} no encontrado para actualizar", employeeId);
                    return new ResourceNotFoundException("Empleado no encontrado con ID: " + employeeId);
                });

        logger.debug("Datos actuales del empleado: {}", employee);
        employee.setNombre(employeeDto.getNombre());
        employee.setApellido(employeeDto.getApellido());
        employee.setEmail(employeeDto.getEmail());
        employee.setTelefono(employeeDto.getTelefono());
        employee.setFechaNacimiento(employeeDto.getFechaNacimiento());
        employee.setEstadoEmployee(employeeDto.getEstadoEmployee());

        Employee updatedEmployee = employeeRepository.save(employee);

        logger.info("Empleado actualizado con éxito: {}", updatedEmployee);
        return EmployeeMapper.mapToEmployeeDto(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        logger.info("Desactivando empleado con ID: {}", employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    logger.error("Empleado con ID {} no encontrado para desactivar", employeeId);
                    return new ResourceNotFoundException("Empleado no encontrado con ID: " + employeeId);
                });

        employee.setEstadoEmployee(EmployeeEstado.INACTIVO);
        employeeRepository.save(employee);

        logger.info("Empleado con ID {} desactivado con éxito", employeeId);
    }

    @Override
    public boolean existsByEmail(String email) {
        boolean exists = employeeRepository.existsByEmail(email);
        logger.debug("Verificación de existencia de email {}: {}", email, exists);
        return exists;
    }
}
