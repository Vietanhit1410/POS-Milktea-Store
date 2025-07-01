package org.example.cnjava_milkteastore.backend.invoice.entity;

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
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int ID;

    @Column(name = "InvoiceCode", unique = true)
    private String invoiceCode;

    @Column(name = "CustomerID")
    private int customerID;

    @Column(name = "EmployeeID")
    private int employeeID;

    @Column(name = "DateCreated", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "TotalPrice")
    private float totalPrice;

    @Column(name = "TotalQuantityDrink")
    private int totalQuantityDrink;

    @Column(name = "TotalQuantityFood")
    private int totalQuantityFood;

    @Column(name = "TotalPriceDrink")
    private float totalPriceDrink;

    @Column(name = "TotalPriceFood")
    private float totalPriceFood;

    @PrePersist
    public void prePersist() {
        if (this.invoiceCode == null || this.invoiceCode.isEmpty()) {
            this.invoiceCode = "IN" + System.currentTimeMillis();
        }
    }
}