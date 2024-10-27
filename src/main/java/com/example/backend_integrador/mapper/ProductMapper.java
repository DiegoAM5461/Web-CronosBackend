package com.example.backend_integrador.mapper;

import com.example.backend_integrador.dto.ProductDto;
import com.example.backend_integrador.entity.Product;

public class ProductMapper {
    public static ProductDto mapToProductDto(Product product){
        return new ProductDto(
            product.getId_product(),
            product.getNombre(),
            product.getDescripcion(),
            product.getPrecio(),
            product.getDisponibilidad(),
            product.getStock()
        );
    }

    public static Product mapToProduct(ProductDto productDto){
        return new Product(
            productDto.getId_product(),
            productDto.getNombre(),
            productDto.getDescripcion(),
            productDto.getPrecio(),
            productDto.getDisponibilidad(),
            productDto.getStock()
        );
    }
}
