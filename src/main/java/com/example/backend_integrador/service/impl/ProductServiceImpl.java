package com.example.backend_integrador.service.impl;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.example.backend_integrador.dto.ProductDto;
import com.example.backend_integrador.entity.Product;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.repository.ProductRepository;
import com.example.backend_integrador.service.ProductService;
import com.example.backend_integrador.mapper.ProductMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = ProductMapper.mapToProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.mapToProductDto(savedProduct);
    }

    @Override
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("El producto con el id_product: " + productId + " no existe"));
        return ProductMapper.mapToProductDto(product);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductMapper::mapToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto updatedProduct) {
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new ResourceNotFoundException("El producto con el id_product: " + productId + " no existe")
        );

        product.setNombre(updatedProduct.getNombre());
        product.setDescripcion(updatedProduct.getDescripcion());
        product.setPrecio(updatedProduct.getPrecio());
        product.setDisponibilidad(updatedProduct.getDisponibilidad());
        product.setStock(updatedProduct.getStock());

        Product updatedProductObj = productRepository.save(product);

        return ProductMapper.mapToProductDto(updatedProductObj);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new ResourceNotFoundException("El producto con el id_product: " + productId + " no existe")
        );
        productRepository.delete(product);
    }
}
