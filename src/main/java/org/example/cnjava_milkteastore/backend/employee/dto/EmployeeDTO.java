package org.example.cnjava_milkteastore.backend.employee.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private int ID;
    private String employeeCode;
    private String name;
    private String address;
    private String gender;
    private LocalDate birthDate;
    private String phone;
}