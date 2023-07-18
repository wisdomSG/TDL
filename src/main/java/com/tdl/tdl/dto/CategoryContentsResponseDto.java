package com.tdl.tdl.dto;

import com.tdl.tdl.entity.Category;
import lombok.Getter;

@Getter
public class CategoryContentsResponseDto {
    //카테고리 이름만 반환하는 Dto
    Long id;
    String  name;
    public CategoryContentsResponseDto(Category category){
        this.id = category.getId();
        this.name = category.getName();
    }
}
