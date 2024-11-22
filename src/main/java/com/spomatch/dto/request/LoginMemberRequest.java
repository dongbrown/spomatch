package com.spomatch.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginMemberRequest {
    private final String loginId;
    private final String password;

    @Builder
    public LoginMemberRequest(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}