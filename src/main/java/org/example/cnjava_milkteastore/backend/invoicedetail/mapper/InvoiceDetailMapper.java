package org.example.cnjava_milkteastore.backend.invoicedetail.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.example.cnjava_milkteastore.backend.invoicedetail.dto.InvoiceDetailDTO;
import org.example.cnjava_milkteastore.backend.invoicedetail.entity.InvoiceDetail;

@Mapper(componentModel = "spring")
public interface InvoiceDetailMapper {
    InvoiceDetailDTO toDTO(InvoiceDetail entity);
    InvoiceDetail toEntity(InvoiceDetailDTO dto);
}