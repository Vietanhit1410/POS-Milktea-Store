package org.example.cnjava_milkteastore.backend.customer.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private int ID;
    private String customerCode;
    private String customerName;
    private String customerPhone;
    private int points;
}