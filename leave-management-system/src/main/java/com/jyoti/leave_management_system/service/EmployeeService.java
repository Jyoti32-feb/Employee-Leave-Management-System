package com.jyoti.leave_management_system.service;

import com.jyoti.leave_management_system.dto.EmployeeRequest;
import com.jyoti.leave_management_system.entity.Employee;
import com.jyoti.leave_management_system.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();

    }

    public Employee createEmployee(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        employee.setName(employeeRequest.getName());
        employee.setEmail(employeeRequest.getEmail());
        employee.setDeptName(employeeRequest.getDepartment());
        employee.setSalary(employeeRequest.getSalary());
        return employeeRepository.save(employee);
    }
    public Employee getEmployeeById(long id) {
        return employeeRepository.findById(id).orElseThrow(()->new RuntimeException("Employee not found with id: " + id));
    }



}
