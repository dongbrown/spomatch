package com.spomatch.dto.request;

import com.spomatch.common.enums.member.Gender;
import com.spomatch.common.enums.member.MemberStatus;
import com.spomatch.common.enums.member.Role;
import com.spomatch.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateMemberRequest {

    private final String loginId;
    private final String password;
    private final String email;
    private final String name;
    private final LocalDate birthDate;
    private final Gender gender;
    private final String address;
    private final String phoneNumber;

    @Builder
    public CreateMemberRequest(String loginId, String password, String email, String name,
                               LocalDate birthDate, Gender gender, String address, String phoneNumber) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Member toEntity() {
        return Member.builder()
                .loginId(loginId)
                .password(password)
                .email(email)
                .name(name)
                .birthDate(birthDate)
                .gender(gender)
                .address(address)
                .phoneNumber(phoneNumber)
                .role(Role.USER)          // 기본값 설정
                .status(MemberStatus.ACTIVE)    // 기본값 설정
                .build();
    }
}