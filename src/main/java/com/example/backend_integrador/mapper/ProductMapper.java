package com.example.backend_integrador.mapper;

import com.example.backend_integrador.dto.ProductDto;
import com.example.backend_integrador.entity.Category;
import com.example.backend_integrador.entity.Product;

public class ProductMapper {
    public static ProductDto mapToProductDto(Product product){
        return new ProductDto(
            product.getProductId(),
            product.getNombre(),
            product.getDescripcion(),
            product.getPrecio(),
            product.getDisponibilidad(),
            product.getStock(),
            product.getDireccionImg(),
            product.getCategory().getCategoryId()
        );
    }

    public static Product mapToProduct(ProductDto productDto, Category category){
        return new Product(
            productDto.getProductId(),
            productDto.getNombre(),
            productDto.getDescripcion(),
            productDto.getPrecio(),
            productDto.getDisponibilidad(),
            productDto.getStock(),
            productDto.getDireccionImg(),
            category
        );
    }
}
