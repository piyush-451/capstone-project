package com.Capstone.Ecommerce.Category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class CategoryMapper {
    public Category toCategory(CategoryRequest request){
        return Category.builder()
                .name(request.name())
                .description(request.description())
                .build();
    }
}
