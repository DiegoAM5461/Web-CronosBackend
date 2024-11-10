package com.example.backend_integrador.service.impl;

import com.example.backend_integrador.dto.EmployeeDto;
import com.example.backend_integrador.entity.Employee;
import com.example.backend_integrador.enums.EmployeeEstado;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.mapper.EmployeeMapper;
import com.example.backend_integrador.repository.EmployeeRepository;
import com.example.backend_integrador.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(EmployeeMapper::mapToEmployeeDto).collect(Collectors.toList());
    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        
        employee.setNombre(employeeDto.getNombre());
        employee.setApellido(employeeDto.getApellido());
        employee.setEmail(employeeDto.getEmail());
        employee.setTelefono(employeeDto.getTelefono());
        employee.setFechaNacimiento(employeeDto.getFechaNacimiento());
        employee.setEstadoEmployee(employeeDto.getEstadoEmployee());
        
        Employee updatedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        
        // EN ESTA PARTE SE CAMBIA EL ESTADO DEL EMPLEADO A INACTIVO -- NO HAY NECESIDAD DE ELIMINAR COMPLETAMENTE
        employee.setEstadoEmployee(EmployeeEstado.INACTIVO);
        employeeRepository.save(employee);
    }
}

