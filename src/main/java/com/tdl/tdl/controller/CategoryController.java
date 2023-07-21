package com.tdl.tdl.controller;

import com.tdl.tdl.dto.CategoryContentsResponseDto;
import com.tdl.tdl.dto.CategoryRequestDto;
import com.tdl.tdl.repository.PostRepository;
import com.tdl.tdl.security.UserDetailsImpl;
import com.tdl.tdl.service.CategoryService;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tdl/admin")

public class CategoryController {
    private final CategoryService categoryService;
    private final PostRepository postRepository;
    Calendar cal = Calendar.getInstance();

    @PostMapping("/category") // 카테고리 등록 어드민만 등록가능
    public CategoryContentsResponseDto registerCategory(@RequestBody CategoryRequestDto RequestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.registerCategory(RequestDto,userDetails.getUser());
    }
    @GetMapping("/category") //존재하는 카테고리 이름 get
    public List<CategoryContentsResponseDto> getCategorys(){
        return categoryService.getCategorys();
    }
}
