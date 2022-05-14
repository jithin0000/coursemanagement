package com.jithin.coursemanagement.repository;

import com.jithin.coursemanagement.models.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role,Long> {
    Optional<Role> findRoleByName(String name);
}
