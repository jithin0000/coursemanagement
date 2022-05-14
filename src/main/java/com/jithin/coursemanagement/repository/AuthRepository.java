package com.jithin.coursemanagement.repository;

import com.jithin.coursemanagement.models.CUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface AuthRepository extends CrudRepository<CUser, Long> {
    Optional<CUser> findCUserByEmail(String email);
}
