package com.example.backend_integrador.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_integrador.dto.CategoryDto;
import com.example.backend_integrador.service.CategoryService;

import lombok.AllArgsConstructor;

@CrossOrigin(origins = {
    "http://localhost:3000", 
    "https://frontend-cronos.vercel.app"
})
@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private CategoryService categoryService;

    // Create a new category REST API
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        logger.info("POST /api/categories - Creando una nueva categoría");
        logger.debug("Datos de la categoría a crear: {}", categoryDto);
        CategoryDto savedCategory = categoryService.createCategory(categoryDto);
        logger.info("Categoría creada con éxito: {}", savedCategory);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    // Get a category by ID REST API
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("categoryId") Long categoryId) {
        logger.info("GET /api/categories/{} - Obtener categoría por ID", categoryId);
        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
        if (categoryDto != null) {
            logger.debug("Categoría encontrada: {}", categoryDto);
        } else {
            logger.warn("No se encontró una categoría con ID: {}", categoryId);
        }
        return ResponseEntity.ok(categoryDto);
    }

    // Get all categories REST API
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        logger.info("GET /api/categories - Obtener todas las categorías");
        List<CategoryDto> categories = categoryService.getAllCategories();
        logger.debug("Cantidad de categorías encontradas: {}", categories.size());
        return ResponseEntity.ok(categories);
    }

    // Update a category by ID REST API
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("categoryId") Long categoryId,
                                                       @RequestBody CategoryDto updatedCategory) {
        logger.info("PUT /api/categories/{} - Actualizando categoría", categoryId);
        logger.debug("Datos para actualizar: {}", updatedCategory);
        CategoryDto categoryDto = categoryService.updateCategory(categoryId, updatedCategory);
        logger.info("Categoría actualizada con éxito: {}", categoryDto);
        return ResponseEntity.ok(categoryDto);
    }

    // Delete a category by ID REST API
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        logger.info("DELETE /api/categories/{} - Eliminando categoría", categoryId);
        categoryService.deleteCategory(categoryId);
        logger.info("Categoría con ID {} eliminada correctamente", categoryId);
        return ResponseEntity.ok("Categoría eliminada correctamente");
    }
}
