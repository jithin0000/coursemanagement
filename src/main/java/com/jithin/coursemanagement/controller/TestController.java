package com.jithin.coursemanagement.controller;

import com.jithin.coursemanagement.dto.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test")
    public ResponseEntity<?> testAuth()
    {
        return ResponseEntity.ok(new ErrorMessage("test successfull"));
    }
}
