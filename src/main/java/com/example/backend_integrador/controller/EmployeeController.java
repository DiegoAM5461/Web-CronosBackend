package com.example.backend_integrador.controller;

import com.example.backend_integrador.dto.EmployeeDto;
import com.example.backend_integrador.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {
    "http://localhost:3000", 
    "https://frontend-cronos.vercel.app"
})
@AllArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        logger.info("POST /api/employees - Creando un nuevo empleado");
        logger.debug("Datos del empleado a crear: {}", employeeDto);
        EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);
        logger.info("Empleado creado con éxito: {}", savedEmployee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("employeeId") Long employeeId) {
        logger.info("GET /api/employees/{} - Obtener empleado por ID", employeeId);
        EmployeeDto employeeDto = employeeService.getEmployeeById(employeeId);
        if (employeeDto != null) {
            logger.debug("Empleado encontrado: {}", employeeDto);
        } else {
            logger.warn("No se encontró un empleado con ID: {}", employeeId);
        }
        return ResponseEntity.ok(employeeDto);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        logger.info("GET /api/employees - Obtener todos los empleados");
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        logger.debug("Cantidad de empleados encontrados: {}", employees.size());
        return ResponseEntity.ok(employees);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("employeeId") Long employeeId, 
                                                       @RequestBody EmployeeDto employeeDto) {
        logger.info("PUT /api/employees/{} - Actualizando empleado", employeeId);
        logger.debug("Datos para actualizar: {}", employeeDto);
        EmployeeDto updatedEmployee = employeeService.updateEmployee(employeeId, employeeDto);
        logger.info("Empleado actualizado con éxito: {}", updatedEmployee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("employeeId") Long employeeId) {
        logger.info("DELETE /api/employees/{} - Eliminando empleado", employeeId);
        employeeService.deleteEmployee(employeeId);
        logger.info("Empleado con ID {} eliminado correctamente", employeeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
