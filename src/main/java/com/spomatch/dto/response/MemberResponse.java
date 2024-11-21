package com.spomatch.dto.response;

import com.spomatch.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {
    private final String loginId;
    private final String name;

    @Builder
    public MemberResponse(Member member) {
        this.loginId = member.getLoginId();
        this.name = member.getName();
    }
}
