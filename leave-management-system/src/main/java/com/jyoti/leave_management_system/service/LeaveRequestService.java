package com.jyoti.leave_management_system.service;

import com.jyoti.leave_management_system.dto.LeaveRequestDto;
import com.jyoti.leave_management_system.entity.Employee;
import com.jyoti.leave_management_system.entity.LeaveRequest;
import com.jyoti.leave_management_system.entity.LeaveStatus;
import com.jyoti.leave_management_system.exception.EmployeeNotFoundException;
import com.jyoti.leave_management_system.exception.InvalidLeaveDateException;
import com.jyoti.leave_management_system.exception.LeaveNotFoundException;
import com.jyoti.leave_management_system.exception.LeaveOverlapException;
import com.jyoti.leave_management_system.repository.EmployeeRepository;
import com.jyoti.leave_management_system.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;
    public final EmployeeRepository employeeRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
    }
    public LeaveRequest applyLeave(LeaveRequestDto leaveRequestDto){
        Employee employee = employeeRepository.findById(leaveRequestDto.getEmployeeId())
                .orElseThrow(()->new EmployeeNotFoundException("Employee not found with id : "+leaveRequestDto.getEmployeeId()));

        if(leaveRequestDto.getStartDate().isAfter(leaveRequestDto.getEndDate())){
            throw new InvalidLeaveDateException("Start date cannot be after end date");
        }
        boolean overlappingLeave =
                leaveRequestRepository
                        .existsByEmployeeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                leaveRequestDto.getEmployeeId(),
                                leaveRequestDto.getEndDate(),
                                leaveRequestDto.getStartDate()
                        );

        if (overlappingLeave) {
            throw new LeaveOverlapException(
                    "Employee already has a leave during this period"
            );
        }
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(leaveRequestDto.getLeaveType());
        leaveRequest.setStartDate(leaveRequestDto.getStartDate());
        leaveRequest.setEndDate(leaveRequestDto.getEndDate());
        leaveRequest.setReason(leaveRequestDto.getReason());

        leaveRequest.setStatus(LeaveStatus.PENDING);
        leaveRequest.setAppliedAt(LocalDate.now());

        return leaveRequestRepository.save(leaveRequest);
    }

    public List<LeaveRequest> getAllLeaveRequests(){
        return leaveRequestRepository.findAll();
    }

    public LeaveRequest getLeaveRequestById(Long id){
        return leaveRequestRepository.findById(id).orElseThrow(()->new LeaveNotFoundException("Leave request not found with id:  + id"));
    }

    public List<LeaveRequest> getLeaveRequestsByEmployeeId(Long employeeId){
        employeeRepository.findById(employeeId).orElseThrow(()->new EmployeeNotFoundException("Employee not found with id : "+employeeId));
        return leaveRequestRepository.findByEmployeeId(employeeId);
    }

}
