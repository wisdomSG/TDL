package com.tdl.tdl.service;

import com.tdl.tdl.dto.CategoryContentsResponseDto;
import com.tdl.tdl.dto.CategoryRequestDto;
import com.tdl.tdl.entity.Category;
import com.tdl.tdl.entity.User;
import com.tdl.tdl.entity.UserRoleEnum;
import com.tdl.tdl.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryContentsResponseDto registerCategory(CategoryRequestDto requestDto, User user) {
        confirmAdminToken(user);
        Category category = new Category(requestDto);
        return new CategoryContentsResponseDto(categoryRepository.save(category));
    }

    public List<CategoryContentsResponseDto> getCategorys() {
        List<CategoryContentsResponseDto> categoryContentsResponseDtoList =  new ArrayList<>();
        List<Category> categoryList =  categoryRepository.findAll();
        for (Category category : categoryList) {
            CategoryContentsResponseDto categoryContentsResponseDto = new CategoryContentsResponseDto(category);
            categoryContentsResponseDtoList.add(categoryContentsResponseDto);
        }
        return categoryContentsResponseDtoList;
    }

    private void confirmAdminToken(User user){
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
        } else {
            throw new IllegalArgumentException("해당 유저가 작성한글이 아닙니다");
        }
    }
}
