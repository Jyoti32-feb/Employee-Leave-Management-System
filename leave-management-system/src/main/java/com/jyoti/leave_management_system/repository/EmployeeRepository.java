package com.jyoti.leave_management_system.repository;

import com.jyoti.leave_management_system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
}
