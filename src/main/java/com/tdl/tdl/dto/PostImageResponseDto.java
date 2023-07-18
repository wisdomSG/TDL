package com.tdl.tdl.dto;

import com.tdl.tdl.entity.PostImage;
import lombok.Getter;

@Getter
public class PostImageResponseDto {
    private String fileName;

    public PostImageResponseDto(PostImage postImage) {
        this.fileName = postImage.getFileName();
    }
}
