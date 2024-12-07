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

import com.example.backend_integrador.dto.CategoryWithProductsDto;
import com.example.backend_integrador.dto.ProductDto;
import com.example.backend_integrador.service.ProductService;

import lombok.AllArgsConstructor;

@CrossOrigin(origins = {
    "http://localhost:3000", 
    "https://frontend-cronos.vercel.app"
})
@AllArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private ProductService productService;

    // Create a new product REST API
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        logger.info("POST /api/products - Creando un nuevo producto");
        logger.debug("Datos del producto a crear: {}", productDto);
        ProductDto savedProduct = productService.createProduct(productDto);
        logger.info("Producto creado con éxito: {}", savedProduct);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // Get a product by ID REST API
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("productId") Long productId) {
        logger.info("GET /api/products/{} - Obtener producto por ID", productId);
        ProductDto productDto = productService.getProductById(productId);
        if (productDto != null) {
            logger.debug("Producto encontrado: {}", productDto);
        } else {
            logger.warn("No se encontró un producto con ID: {}", productId);
        }
        return ResponseEntity.ok(productDto);
    }

    // Get all products REST API
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        logger.info("GET /api/products - Obtener todos los productos");
        List<ProductDto> products = productService.getAllProducts();
        logger.debug("Cantidad de productos encontrados: {}", products.size());
        return ResponseEntity.ok(products);
    }

    // Update a product by ID REST API
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("productId") Long productId,
                                                    @RequestBody ProductDto updatedProduct) {
        logger.info("PUT /api/products/{} - Actualizando producto", productId);
        logger.debug("Datos para actualizar: {}", updatedProduct);
        ProductDto productDto = productService.updateProduct(productId, updatedProduct);
        logger.info("Producto actualizado con éxito: {}", productDto);
        return ResponseEntity.ok(productDto);
    }

    // Delete a product by ID REST API
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId) {
        logger.info("DELETE /api/products/{} - Eliminando producto", productId);
        productService.deleteProduct(productId);
        logger.info("Producto con ID {} eliminado correctamente", productId);
        return ResponseEntity.ok("Producto eliminado correctamente");
    }

    // Get products by category ID REST API
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<CategoryWithProductsDto> getProductsByCategoryId(@PathVariable Long categoryId) {
        logger.info("GET /api/products/category/{} - Obtener productos por categoría", categoryId);
        CategoryWithProductsDto categoryWithProducts = productService.findProductsByCategoryId(categoryId);
        logger.debug("Productos encontrados para la categoría {}: {}", categoryId, categoryWithProducts);
        return ResponseEntity.ok(categoryWithProducts);
    }
}
