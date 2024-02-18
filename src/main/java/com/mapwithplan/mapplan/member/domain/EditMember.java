package com.mapwithplan.mapplan.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class EditMember {

    private final String statusMessage;

    @NotEmpty
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "휴대폰 번호의 형식이 올바르지 않습니다.")
    private final String phone;


    public EditMember(@JsonProperty("statusMessage") String statusMessage,
                      @JsonProperty("phone") String phone) {
        this.statusMessage = statusMessage;
        this.phone = phone;
    }

}
