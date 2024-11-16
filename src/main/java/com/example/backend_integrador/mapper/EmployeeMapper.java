package com.example.backend_integrador.mapper;

import com.example.backend_integrador.dto.EmployeeDto;
import com.example.backend_integrador.entity.Employee;
import com.example.backend_integrador.enums.EmployeeEstado;

public class EmployeeMapper {
    public static EmployeeDto mapToEmployeeDto(Employee employee) {
        return new EmployeeDto(
                employee.getEmployeeId(),
                employee.getNombre(),
                employee.getApellido(),
                employee.getEmail(),
                employee.getTelefono(),
                employee.getFechaNacimiento(),
                employee.getEstadoEmployee());
    }

    public static Employee mapToEmployee(EmployeeDto employeeDto) {
        return new Employee(
                employeeDto.getEmployeeId(),
                employeeDto.getNombre(),
                employeeDto.getApellido(),
                employeeDto.getEmail(),
                employeeDto.getTelefono(),
                employeeDto.getFechaNacimiento(),
                EmployeeEstado.INACTIVO // Asigna INACTIVO por defecto
        );
    }

}
