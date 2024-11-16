package com.example.backend_integrador.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryWithProductsDto {
    private String nombreCategory;
    private List<ProductDto> products;
}
