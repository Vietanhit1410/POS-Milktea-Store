package org.example.cnjava_milkteastore.backend.account.entity;

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
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int ID;

    @Column(name = "UserName", unique = true)
    private String userName;

    @Column(name = "Password")
    private String password;

    @Column(name = "Role")
    private String role;

    @Column(name = "EmployeeID")
    private int employeeID;
}