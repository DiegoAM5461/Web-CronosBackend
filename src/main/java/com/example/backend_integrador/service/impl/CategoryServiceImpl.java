package com.example.backend_integrador.service.impl;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.example.backend_integrador.dto.CategoryDto;
import com.example.backend_integrador.entity.Category;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.repository.CategoryRepository;
import com.example.backend_integrador.service.CategoryService;
import com.example.backend_integrador.mapper.CategoryMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = CategoryMapper.mapToCategory(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.mapToCategoryDto(savedCategory);
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("La categoria con el categoryId: " + categoryId + " no existe"));
        return CategoryMapper.mapToCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryMapper::mapToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto updatedCategory) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
            () -> new ResourceNotFoundException("La categoria con el categoryId: " + categoryId + " no existe")
        );
    
        // Usa los métodos correctos generados por Lombok para establecer los valores
        category.setNombreCategory(updatedCategory.getNombreCategory());
        category.setDescripcionCategory(updatedCategory.getDescripcionCategory());
    
        Category updatedCategoryObj = categoryRepository.save(category);
    
        return CategoryMapper.mapToCategoryDto(updatedCategoryObj);
    }
    

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
            () -> new ResourceNotFoundException("La categoria con el categoryId: " + categoryId + " no existe")
        );
        categoryRepository.delete(category);
    }
}