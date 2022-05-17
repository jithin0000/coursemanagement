package com.jithin.coursemanagement.converters;

import com.jithin.coursemanagement.dto.StudentRegisterRequest;
import com.jithin.coursemanagement.models.UserProfile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class StudentRegisterToProfileConverter implements Converter<StudentRegisterRequest, UserProfile> {
    @Override
    public UserProfile convert(StudentRegisterRequest source) {
        UserProfile profile = new UserProfile();
        profile.setUsername(source.getUsername());
        profile.setPhoneNumber(source.getPhoneNumber());
        profile.setProfilePicUrl(source.getProfilePicUrl());
        return profile;
    }
}
