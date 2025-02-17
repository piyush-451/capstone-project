package com.Capstone.Ecommerce.Category;

import com.Capstone.Ecommerce.Exceptions.ResourceAlreadyPresentException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepo categoryRepo;
    private final CategoryMapper categoryMapper;

    public Category addCategory(@Valid CategoryRequest request) {
        try{
            Category category = categoryMapper.toCategory(request);
            return categoryRepo.save(category);
        }
        catch (Exception e){
            throw new ResourceAlreadyPresentException(e.getMessage());
        }

    }

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }
}
