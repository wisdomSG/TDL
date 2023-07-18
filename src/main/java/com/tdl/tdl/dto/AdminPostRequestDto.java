package com.tdl.tdl.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AdminPostRequestDto {
    private String content;
    private List<String> postImageList = new ArrayList<>();
}
