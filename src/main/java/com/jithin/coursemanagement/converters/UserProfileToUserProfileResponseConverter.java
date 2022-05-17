package com.jithin.coursemanagement.converters;

import com.jithin.coursemanagement.dto.UserProfileResponse;
import com.jithin.coursemanagement.models.UserProfile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserProfileToUserProfileResponseConverter implements Converter<UserProfile, UserProfileResponse> {
    @Override
    public UserProfileResponse convert(UserProfile source) {
        return new UserProfileResponse(
                source.getId(),
                source.getUser().getEmail(),
                source.getUsername(),
                source.getPhoneNumber(),
                source.getProfilePicUrl()
        );
    }
}
