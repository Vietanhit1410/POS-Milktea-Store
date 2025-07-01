package org.example.cnjava_milkteastore.backend.product.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private int ID;
    private String productCode;
    private int stock;
    private String productType;
    private float price;
    private int points;
    private String productName;
    private String productImage;
}