package org.example.cnjava_milkteastore.backend.report.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private int ID;
    private String reportCode;
    private LocalDateTime createAt;
    private float totalPrice;
    private int totalProductSold;
    private int totalProductFood;
    private int totalProductDrink;
    private int quantityDrink;
}