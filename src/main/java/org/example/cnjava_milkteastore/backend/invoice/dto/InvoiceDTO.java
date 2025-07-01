package org.example.cnjava_milkteastore.backend.invoice.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private int ID;
    private String invoiceCode;
    private int customerID;
    private int employeeID;
    private LocalDateTime dateCreated;
    private float totalPrice;
    private int totalQuantityDrink;
    private int totalQuantityFood;
    private float totalPriceDrink;
    private float totalPriceFood;
}