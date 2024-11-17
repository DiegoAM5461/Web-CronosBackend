package com.example.backend_integrador.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "table_cronos")
public class TableCronos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tableCronosId")
    private Long tableCronosId;

    @Column(name = "tableNumero")
    private String tableNumero;

    @Column(name = "tableQR")
    private String tableQR;
}
