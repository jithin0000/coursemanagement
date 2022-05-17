package com.jithin.coursemanagement.repository;

import com.jithin.coursemanagement.models.UserProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserProfileRepository  extends CrudRepository<UserProfile, Long> {
    List<UserProfile> findAllByCreatedById(Long id);
}
