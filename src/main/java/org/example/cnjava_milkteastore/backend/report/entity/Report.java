package org.example.cnjava_milkteastore.backend.report.entity;

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
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int ID;

    @Column(name = "ReportCode", unique = true)
    private String reportCode;

    @Column(name = "CreateAt")
    private LocalDateTime createAt;

    @Column(name = "TotalPrice")
    private float totalPrice;

    @Column(name = "TotalProductSold")
    private int totalProductSold;

    @Column(name = "TotalProductFood")
    private int totalProductFood;

    @Column(name = "TotalProductDrink")
    private int totalProductDrink;

    @Column(name = "QuantityDrink")
    private int quantityDrink;

    @PrePersist
    public void prePersist() {
        if (this.reportCode == null || this.reportCode.isEmpty()) {
            this.reportCode = "RE" + System.currentTimeMillis();
        }
    }
}