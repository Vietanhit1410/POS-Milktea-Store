package org.example.cnjava_milkteastore.backend.invoicedetail.entity;

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
@Table(name = "invoicedetail")
public class InvoiceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int ID;

    @Column(name = "InvoiceID")
    private int invoiceID;

    @Column(name = "ProductID")
    private int productID;

    @Column(name = "Quantity")
    private int quantity;

    @Column(name = "Price")
    private float price;

    @Column(name = "ProductName")
    private String productName;

    @Column(name = "ProductType")
    private String productType;
}