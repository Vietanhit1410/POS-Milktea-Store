package org.example.cnjava_milkteastore.backend.employee.entity;

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
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int ID;

    @Column(name = "EmployeeCode")
    private String employeeCode;

    @Column(name = "Name")
    private String Name;


    @Column(name = "Address")
    private String address;

    @Column(name = "Gender")
    private String gender;

    @Column(name = "BirthDate")
    private LocalDate birthDate;

    @Column(name = "Phone")
    private String phone;

    @PrePersist
    public void prePersist() {
        if (this.employeeCode == null || this.employeeCode.isEmpty()) {
            this.employeeCode = "EM" + System.currentTimeMillis();
        }
    }
}