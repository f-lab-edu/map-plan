package com.mapwithplan.mapplan.loginlogout.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class Login {

    @Email(message = "이메일을 올바르게 작성하세요.")
    private final String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", message = "Invalid password pattern")
    private final String password;


    public Login(@JsonProperty("email") String email,
                 @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }
}
