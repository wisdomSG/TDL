package com.tdl.tdl.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

//    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$")
    @NotBlank
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
    @NotBlank
    private String password;

    @NotBlank
    private String profilename;

    private boolean admin = false;

    private String adminToken = "";

}
