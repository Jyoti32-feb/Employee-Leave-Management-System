package com.jyoti.leave_management_system.controller;

import com.jyoti.leave_management_system.dto.LeaveRequestDto;
import com.jyoti.leave_management_system.dto.LeaveResponseDto;
import com.jyoti.leave_management_system.dto.UpdateLeaveStatusDto;
import com.jyoti.leave_management_system.entity.LeaveRequest;
import com.jyoti.leave_management_system.entity.LeaveStatus;
import com.jyoti.leave_management_system.repository.LeaveRequestRepository;
import com.jyoti.leave_management_system.service.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(
            LeaveRequestService leaveRequestService) {

        this.leaveRequestService = leaveRequestService;
    }


    @PostMapping
    public ResponseEntity<LeaveResponseDto> applyLeave(
            @Valid @RequestBody LeaveRequestDto leaveRequestDto) {

        LeaveResponseDto leaveResponse =
                leaveRequestService.applyLeave(leaveRequestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(leaveResponse);
    }

    @GetMapping
    public ResponseEntity<Page<LeaveResponseDto>> getAllLeaveRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Page<LeaveResponseDto> leaveRequests =
                leaveRequestService.getAllLeaveRequests(
                        page,
                        size,
                        sortBy,
                        direction
                );

        return ResponseEntity.ok(leaveRequests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveResponseDto> getLeaveRequestById(
            @PathVariable Long id) {

        LeaveResponseDto leaveResponse =
                leaveRequestService.getLeaveRequestById(id);

        return ResponseEntity.ok(leaveResponse);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveResponseDto>> getLeavesByEmployeeId(
            @PathVariable Long employeeId) {

        List<LeaveResponseDto> leaveRequests =
                leaveRequestService.getLeavesByEmployeeId(employeeId);

        return ResponseEntity.ok(leaveRequests);
    }
    @PutMapping("/{leaveId}/status")
    public ResponseEntity<LeaveResponseDto> updateLeaveStatus(
            @PathVariable Long leaveId,
            @Valid @RequestBody UpdateLeaveStatusDto statusDto) {

        LeaveResponseDto leaveResponse =
                leaveRequestService.updateLeaveStatus(
                        leaveId,
                        statusDto.getStatus()
                );

        return ResponseEntity.ok(leaveResponse);
    }
    @PutMapping("/{leaveId}/cancel")
    public ResponseEntity<LeaveResponseDto> cancelLeave(
            @PathVariable Long leaveId) {

        LeaveResponseDto leaveResponse =
                leaveRequestService.cancelLeave(leaveId);

        return ResponseEntity.ok(leaveResponse);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LeaveResponseDto>> getLeavesByStatus(
            @PathVariable LeaveStatus status) {

        List<LeaveResponseDto> leaveRequests =
                leaveRequestService.getLeavesByStatus(status);

        return ResponseEntity.ok(leaveRequests);
    }
}

