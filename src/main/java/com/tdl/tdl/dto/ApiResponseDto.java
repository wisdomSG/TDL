package com.tdl.tdl.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponseDto {
    private String errorMessage;
    private int statusCode;
}
