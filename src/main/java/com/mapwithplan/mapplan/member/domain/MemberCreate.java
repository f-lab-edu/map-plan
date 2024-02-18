package com.mapwithplan.mapplan.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

/**
 * 회원 가입시 사용되는 DTO 입니다.
 */
@Getter
public class MemberCreate {


    @Email(message = "이메일을 올바르게 작성하세요.")
    private final String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", message = "Invalid password pattern")
    private final String password;

    @NotBlank
    private final String name;

    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "Invalid phone number")
    private final String phone;


    @Builder
    public MemberCreate(
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("name")String name,
            @JsonProperty("phone") String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }


}
