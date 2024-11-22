package com.spomatch.common.enums.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("관리자"),
    USER("일반사용자");

    private final String description;
}