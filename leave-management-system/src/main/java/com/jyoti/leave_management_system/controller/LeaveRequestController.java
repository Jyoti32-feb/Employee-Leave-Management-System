package com.jyoti.leave_management_system.controller;

import com.jyoti.leave_management_system.dto.LeaveRequestDto;
import com.jyoti.leave_management_system.dto.LeaveResponseDto;
import com.jyoti.leave_management_system.dto.UpdateLeaveStatusDto;
import com.jyoti.leave_management_system.entity.LeaveRequest;
import com.jyoti.leave_management_system.repository.LeaveRequestRepository;
import com.jyoti.leave_management_system.service.LeaveRequestService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<LeaveResponseDto>> getAllLeaveRequests() {

        List<LeaveResponseDto> leaveRequests =
                leaveRequestService.getAllLeaveRequests();

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
    @PutMapping("{leaveid}/status")
    public ResponseEntity<LeaveRequest> updateLeaveStatus(@PathVariable Long leaveid, @Valid @RequestBody UpdateLeaveStatusDto updateLeaveStatusDto){
        LeaveRequest leaveRequest=leaveRequestService.updateLeaveStatus(leaveid,updateLeaveStatusDto.getStatus());
        return ResponseEntity.status(HttpStatus.OK).body(leaveRequest);
    }
    @PutMapping("/{leaveId}/cancel")
    public ResponseEntity<LeaveRequest> cancelLeave(
            @PathVariable Long leaveId) {

        LeaveRequest leaveRequest =
                leaveRequestService.cancelLeave(leaveId);

        return ResponseEntity.ok(leaveRequest);
    }
}

