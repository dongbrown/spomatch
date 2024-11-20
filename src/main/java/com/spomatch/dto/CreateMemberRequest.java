package com.spomatch.dto;

import com.spomatch.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateMemberRequest {

    private final String loginId;
    private final String password;
    private final String name;

    @Builder
    public CreateMemberRequest(String loginId, String password, String name) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
    }

    public Member toEntity() {
        return Member.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .build();
    }
}
