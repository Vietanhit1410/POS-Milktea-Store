package org.example.cnjava_milkteastore.backend.invoicedetail.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailDTO {
    private int ID;
    private int invoiceID;
    private int productID;
    private int quantity;
    private float price;
    private String productName;
    private String productType;
}