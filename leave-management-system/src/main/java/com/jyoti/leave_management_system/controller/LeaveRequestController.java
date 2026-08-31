package com.jyoti.leave_management_system.controller;

import com.jyoti.leave_management_system.dto.LeaveRequestDto;
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
    public ResponseEntity<LeaveRequest> applyLeave(@Valid @RequestBody LeaveRequestDto leaveRequestDto) {
        LeaveRequest leaveRequest = leaveRequestService.applyLeave(leaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(leaveRequest);

    }
    @GetMapping
    public ResponseEntity<List<LeaveRequest>> getAllLeaveRequests(){
        List<LeaveRequest>leaveRequest=leaveRequestService.getAllLeaveRequests();
        return  ResponseEntity.status(HttpStatus.OK).body(leaveRequest);
    }
}

