package org.example.cnjava_milkteastore.backend.product.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int ID;

    @Column(name = "ProductCode", unique = true)
    private String productCode;

    @PrePersist
    public void prePersist() {
            this.productCode = "PR" + System.currentTimeMillis();
    }
    @PreUpdate
    public void preUpdate() {
        this.productCode = "PR" + System.currentTimeMillis();
    }
    @Column(name = "Stock")
    private int stock;

    @Column(name = "ProductType")
    private String productType;

    @Column(name = "Price")
    private float price;

    @Column(name = "Points")
    private int points;

    @Column(name = "ProductName", nullable = false)
    private String productName;

    @Column(name = "ProductImage")
    private String productImage;
}