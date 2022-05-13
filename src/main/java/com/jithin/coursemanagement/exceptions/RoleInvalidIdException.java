package com.jithin.coursemanagement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RoleInvalidIdException extends RuntimeException {
    public RoleInvalidIdException(String s) {
        super(s);
    }
}
