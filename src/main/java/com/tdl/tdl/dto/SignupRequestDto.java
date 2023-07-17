package com.tdl.tdl.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    @NotBlank
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
    @NotBlank
    private String password;

    @NotBlank
    private String username;

    private boolean admin = false;
    private String adminToken = "";

}
