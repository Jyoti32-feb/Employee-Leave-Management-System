package com.jyoti.leave_management_system.dto;

import com.jyoti.leave_management_system.entity.LeaveStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateLeaveStatusDto {

    @NotNull(message = "Leave status is required")
    private LeaveStatus status;

    public UpdateLeaveStatusDto() {
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }
}