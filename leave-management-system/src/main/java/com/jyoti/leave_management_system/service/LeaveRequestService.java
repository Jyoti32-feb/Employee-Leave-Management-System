package com.jyoti.leave_management_system.service;

import com.jyoti.leave_management_system.dto.LeaveRequestDto;
import com.jyoti.leave_management_system.dto.LeaveResponseDto;
import com.jyoti.leave_management_system.entity.Employee;
import com.jyoti.leave_management_system.entity.LeaveRequest;
import com.jyoti.leave_management_system.entity.LeaveStatus;
import com.jyoti.leave_management_system.exception.*;
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
    public LeaveResponseDto applyLeave(LeaveRequestDto leaveRequestDto){
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

        LeaveRequest savedLeave =
                leaveRequestRepository.save(leaveRequest);

        return mapToResponseDto(savedLeave);
    }

    public List<LeaveResponseDto> getAllLeaveRequests() {

        return leaveRequestRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public LeaveResponseDto getLeaveRequestById(Long id) {

        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() ->
                        new LeaveNotFoundException(
                                "Leave request not found with id: " + id
                        )
                );

        return mapToResponseDto(leaveRequest);
    }

    public List<LeaveResponseDto> getLeavesByEmployeeId(Long employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with id: " + employeeId
                        )
                );

        return leaveRequestRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public LeaveRequest updateLeaveStatus(Long id, LeaveStatus newStatus){
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id).
                orElseThrow(()->new LeaveNotFoundException("Leave request not found with id:  "+ id));

        if(leaveRequest.getStatus()!=LeaveStatus.PENDING){
            throw new InvalidLeaveStatusException("Only pending leave requests can be approved or rejected");
        }

        if(newStatus!=LeaveStatus.APPROVED&&newStatus!=LeaveStatus.REJECTED){
            throw new InvalidLeaveStatusException("Leave can only be approved or rejected");
        }


        leaveRequest.setStatus(newStatus);
        return leaveRequestRepository.save(leaveRequest);



    }

    public LeaveRequest cancelLeave(Long leaveId){
        LeaveRequest leaveRequest = leaveRequestRepository
                .findById(leaveId)
                .orElseThrow(() ->
                        new LeaveNotFoundException(
                                "Leave request not found with id: " + leaveId
                        )
                );
        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidLeaveStatusException(
                    "Only pending leave requests can be cancelled"
            );
        }

        leaveRequest.setStatus(LeaveStatus.CANCELLED);

        return leaveRequestRepository.save(leaveRequest);

    }
    private LeaveResponseDto mapToResponseDto(LeaveRequest leaveRequest) {

        LeaveResponseDto responseDto = new LeaveResponseDto();

        responseDto.setLeaveId(leaveRequest.getId());

        responseDto.setEmployeeId(
                leaveRequest.getEmployee().getId()
        );

        responseDto.setEmployeeName(
                leaveRequest.getEmployee().getName()
        );

        responseDto.setLeaveType(leaveRequest.getLeaveType());
        responseDto.setStartDate(leaveRequest.getStartDate());
        responseDto.setEndDate(leaveRequest.getEndDate());
        responseDto.setReason(leaveRequest.getReason());
        responseDto.setStatus(leaveRequest.getStatus());
        responseDto.setAppliedAt(leaveRequest.getAppliedAt());

        return responseDto;
    }


}
