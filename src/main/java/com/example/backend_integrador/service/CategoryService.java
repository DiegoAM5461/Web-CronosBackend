package com.example.backend_integrador.service;

import java.util.List;
import com.example.backend_integrador.dto.CategoryDto;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto getCategoryById(Long categoryId);

    List<CategoryDto> getAllCategories();

    CategoryDto updateCategory(Long categoryId, CategoryDto updatedCategory);

    void deleteCategory(Long categoryId);
}
