package com.tdl.tdl.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostRequestDto {
    private String contents;
    List<MultipartFile> multipartFiles;
}
