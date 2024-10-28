package com.example.backend_integrador.mapper;

import com.example.backend_integrador.dto.CategoryDto;
import com.example.backend_integrador.entity.Category;

public class CategoryMapper {
    public static CategoryDto mapToCategoryDto(Category category){
        return new CategoryDto(
            category.getCategoryId(),
            category.getNombreCategory(),
            category.getDescripcionCategory()
        );
    }


    public static Category mapToCategory(CategoryDto categoryDto){
        return new Category(
            categoryDto.getCategoryId(),
            categoryDto.getNombreCategory(),
            categoryDto.getDescripcionCategory()
        );
    }

}
