package org.example.cnjava_milkteastore.backend.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.example.cnjava_milkteastore.backend.product.dto.ProductDTO;
import org.example.cnjava_milkteastore.backend.product.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product entity);
    Product toEntity(ProductDTO dto);
}