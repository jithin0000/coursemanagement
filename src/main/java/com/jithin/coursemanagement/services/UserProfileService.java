package com.jithin.coursemanagement.services;

import com.jithin.coursemanagement.models.UserProfile;
import com.jithin.coursemanagement.repository.UserProfileRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository profileRepository;

    public UserProfile create(@NotNull UserProfile userProfile) {
       return profileRepository.save(userProfile);
    }

    public List<UserProfile> getProfileOfUser(long id) {
        return profileRepository.findAllByCreatedById(id);
    }
}
