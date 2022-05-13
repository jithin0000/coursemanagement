package com.jithin.coursemanagement.controller;

import com.jithin.coursemanagement.dto.RoleRequest;
import com.jithin.coursemanagement.exceptions.RoleInvalidIdException;
import com.jithin.coursemanagement.models.Role;
import com.jithin.coursemanagement.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class RoleController {

    @Autowired
    private RoleService service;

    @PostMapping("/api/role/create")
    public ResponseEntity<?> create(@Valid @RequestBody RoleRequest request)
    {
        Role role =new Role();
        role.setName("ROLE_"+request.getName());
       return ResponseEntity.status(HttpStatus.CREATED).body(service.create(role));
    }

    @GetMapping("/api/role/all")
    public ResponseEntity<?> getAll()
    {
        return ResponseEntity.ok(service.getAll());
    }


    @GetMapping("/api/role/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id)
    {
        Role role = service.findById(id).orElseThrow(
                ()->new RoleInvalidIdException("no role with this id "+id)
        );
        return ResponseEntity.ok(role);
    }

}
