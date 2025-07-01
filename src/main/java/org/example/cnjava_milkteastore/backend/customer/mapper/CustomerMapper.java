package org.example.cnjava_milkteastore.backend.customer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.example.cnjava_milkteastore.backend.customer.dto.CustomerDTO;
import org.example.cnjava_milkteastore.backend.customer.entity.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO toDTO(Customer entity);
    Customer toEntity(CustomerDTO dto);
}