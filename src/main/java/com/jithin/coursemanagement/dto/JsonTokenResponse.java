package com.jithin.coursemanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JsonTokenResponse {
    private String token;

    public JsonTokenResponse(String token) {
        this.token = token;
    }
}
