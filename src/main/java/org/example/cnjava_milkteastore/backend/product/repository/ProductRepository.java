package org.example.cnjava_milkteastore.backend.product.repository;

import org.springframework.stereotype.Repository;
import org.example.cnjava_milkteastore.backend.product.entity.Product;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
}