package com.example.backend_integrador.service;

import java.util.List;

import com.example.backend_integrador.dto.CategoryWithProductsDto;
import com.example.backend_integrador.dto.ProductDto;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);

    ProductDto getProductById(Long productId);

    List<ProductDto> getAllProducts();

    ProductDto updateProduct(Long productId, ProductDto updatedProduct);

    void deleteProduct(Long productId);

    CategoryWithProductsDto findProductsByCategoryId(Long categoryId);

}