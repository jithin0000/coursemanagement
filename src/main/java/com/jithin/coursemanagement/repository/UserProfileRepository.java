package com.jithin.coursemanagement.repository;

import com.jithin.coursemanagement.models.UserProfile;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository  extends CrudRepository<UserProfile, Long> {
}
