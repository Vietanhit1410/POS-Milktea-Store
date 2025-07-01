package org.example.cnjava_milkteastore.backend.account.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private int ID;
    private String userName;
    private String password;
    private String role;
    private int employeeID;
}