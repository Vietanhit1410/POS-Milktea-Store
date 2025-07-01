package org.example.cnjava_milkteastore.backend.invoice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.example.cnjava_milkteastore.backend.invoice.dto.InvoiceDTO;
import org.example.cnjava_milkteastore.backend.invoice.entity.Invoice;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    InvoiceDTO toDTO(Invoice entity);
    Invoice toEntity(InvoiceDTO dto);
}