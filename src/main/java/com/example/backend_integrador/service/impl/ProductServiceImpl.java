package com.example.backend_integrador.service.impl;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.example.backend_integrador.entity.Category;
import com.example.backend_integrador.dto.CategoryWithProductsDto;
import com.example.backend_integrador.dto.ProductDto;
import com.example.backend_integrador.entity.Product;
import com.example.backend_integrador.exceptions.ResourceNotFoundException;
import com.example.backend_integrador.repository.CategoryRepository;
import com.example.backend_integrador.repository.ProductRepository;
import com.example.backend_integrador.service.ProductService;
import com.example.backend_integrador.mapper.ProductMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        // Buscar la categoría usando el ID de la categoría
        Category category = categoryRepository.findById(productDto.getIdCategory())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "La categoría con id_category: " + productDto.getIdCategory() + " no existe"));

        // Mapear el producto
        Product product = ProductMapper.mapToProduct(productDto, category);
        Product savedProduct = productRepository.save(product);

        // Retornar el producto guardado como DTO
        return ProductMapper.mapToProductDto(savedProduct);
    }

    @Override
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El producto con el id_product: " + productId + " no existe"));
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
        // Buscar el producto existente
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("El producto con id_product: " + productId + " no existe"));

        // Buscar la categoría usando el ID de la categoría proporcionada
        Category category = categoryRepository.findById(updatedProduct.getIdCategory())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "La categoría con id_category: " + updatedProduct.getIdCategory() + " no existe"));

        // Actualizar los atributos del producto
        product.setNombre(updatedProduct.getNombre());
        product.setDescripcion(updatedProduct.getDescripcion());
        product.setPrecio(updatedProduct.getPrecio());
        product.setDisponibilidad(updatedProduct.getDisponibilidad());
        product.setStock(updatedProduct.getStock());
        product.setDireccionImg(updatedProduct.getDireccionImg());
        product.setCategory(category); // Asignar la nueva categoría

        Product updatedProductObj = productRepository.save(product);
        return ProductMapper.mapToProductDto(updatedProductObj);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("El producto con el id_product: " + productId + " no existe"));
        productRepository.delete(product);
    }

    @Override
    public CategoryWithProductsDto findProductsByCategoryId(Long categoryId) {
        // Buscar la categoría por ID
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("La categoría con id_category: " + categoryId + " no existe"));

        // Buscar los productos relacionados a la categoría
        List<Product> products = productRepository.findByCategoryId(categoryId);

        // Mapear los productos a DTOs
        List<ProductDto> productDtos = products.stream()
                .map(ProductMapper::mapToProductDto)
                .collect(Collectors.toList());

        // Retornar el DTO con nombre de categoría y productos
        return new CategoryWithProductsDto(category.getNombreCategory(), productDtos);
    }

}
