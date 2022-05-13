package com.jithin.coursemanagement.services;

import com.jithin.coursemanagement.models.Role;
import com.jithin.coursemanagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;


    public Role create(Role role) {
        return repository.save(role);
    }

    public List<Role> getAll() {
        return (List<Role>) repository.findAll();
    }

    public Optional<Role> findById(long id) {
        return repository.findById(id);
    }
}
