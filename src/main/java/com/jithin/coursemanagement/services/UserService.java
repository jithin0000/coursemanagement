package com.jithin.coursemanagement.services;

import com.jithin.coursemanagement.models.CUser;
import com.jithin.coursemanagement.repository.AuthRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    AuthRepository authRepository;

    public Optional<CUser> findUserByEmail(String email){
        return authRepository.findCUserByEmail(email);

    }

    public CUser create(@NotNull CUser user) {
        return authRepository.save(user);
    }
}
