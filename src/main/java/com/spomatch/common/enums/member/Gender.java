package com.spomatch.common.enums.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    M("남성"),
    F("여성");

    private final String description;
}
