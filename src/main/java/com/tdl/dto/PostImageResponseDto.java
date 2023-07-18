package com.tdl.dto;

import com.tdl.entity.PostImage;
import lombok.Getter;

@Getter
public class PostImageResponseDto {
    private String fileName;

    public PostImageResponseDto(PostImage postImage) {
        this.fileName = postImage.getFileName();
    }
}
