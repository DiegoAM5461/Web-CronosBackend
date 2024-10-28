package com.example.backend_integrador.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "category" )
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    private Long categoryId;

    @Column(name = "nombreCategory")
    private String nombreCategory;

    @Column(name = "descripcionCategory")   
    private String descripcionCategory;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    // Constructor sin el atributo 'products' (opcional)
    public Category(Long idCategory, String nombreCategory, String descripcionCategory) {
        this.categoryId = idCategory;
        this.nombreCategory = nombreCategory;
        this.descripcionCategory = descripcionCategory;
    }
}
