package org.example.cnjava_milkteastore.backend.customer.entity;

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
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int ID;

    @Column(name = "CustomerCode", unique = true)
    private String customerCode;

    @Column(name = "CustomerName", nullable = false)
    private String customerName;

    @Column(name = "CustomerPhone")
    private String customerPhone;

    @Column(name = "Points")
    private int points;

    @PrePersist
    public void prePersist() {
        if (this.customerCode == null || this.customerCode.isEmpty()) {
            this.customerCode = "CU" + System.currentTimeMillis();
        }
    }
}