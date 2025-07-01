package org.example.cnjava_milkteastore.backend.employee.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.example.cnjava_milkteastore.backend.employee.dto.EmployeeDTO;
import org.example.cnjava_milkteastore.backend.employee.entity.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeDTO toDTO(Employee entity);
    Employee toEntity(EmployeeDTO dto);
}