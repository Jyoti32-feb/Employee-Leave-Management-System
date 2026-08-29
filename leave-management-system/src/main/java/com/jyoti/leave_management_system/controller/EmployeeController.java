package com.jyoti.leave_management_system.controller;

import com.jyoti.leave_management_system.dto.EmployeeRequest;
import com.jyoti.leave_management_system.entity.Employee;
import com.jyoti.leave_management_system.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @Valid @RequestBody EmployeeRequest employeeRequest) {

        Employee employee = employeeService.createEmployee(employeeRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }



}
