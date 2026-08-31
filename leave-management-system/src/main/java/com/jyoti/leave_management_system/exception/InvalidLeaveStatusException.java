package com.jyoti.leave_management_system.exception;

public class InvalidLeaveStatusException extends RuntimeException{
    public InvalidLeaveStatusException(String message){
        super(message);
    }
}
