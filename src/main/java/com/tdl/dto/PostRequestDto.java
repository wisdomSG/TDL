package com.tdl.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PostRequestDto {
    private String contents;
    private List<String> postImageList = new ArrayList<>();
}