package org.example.cnjava_milkteastore.backend.product.service;

import org.example.cnjava_milkteastore.backend.product.dto.ProductDTO;
import org.example.cnjava_milkteastore.backend.product.entity.Product;
import org.example.cnjava_milkteastore.backend.product.repository.ProductRepository;
import org.example.cnjava_milkteastore.backend.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;

    @Transactional
    public ProductDTO createOrUpdate(ProductDTO dto) {
        Product entity;
        if (dto.getID() != 0) {
            entity = productRepository.findById(dto.getID()).orElse(new Product());
            entity.setID(dto.getID());
            entity.setProductCode(dto.getProductCode());
            entity.setStock(dto.getStock());
            entity.setProductType(dto.getProductType());
            entity.setPrice(dto.getPrice());
            entity.setPoints(dto.getPoints());
            entity.setProductName(dto.getProductName());
            entity.setProductImage(dto.getProductImage());
        } else {
            entity = productMapper.toEntity(dto);
        }
        Product saved = productRepository.save(entity);
        return productMapper.toDTO(saved);
    }

    public void delete(ProductDTO dto) {
        Product entity = productMapper.toEntity(dto);
        productRepository.delete(entity);
    }

    public void deleteById(int id) {
        productRepository.deleteById(id);
    }

    public void deleteAll() {
        productRepository.deleteAll();
    }

    public List<ProductDTO> getAll() {
        return ((List<Product>) productRepository.findAll())
                .stream().map(productMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<ProductDTO> getById(int id) {
        return productRepository.findById(id).map(productMapper::toDTO);
    }

}