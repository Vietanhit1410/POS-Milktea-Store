package org.example.cnjava_milkteastore.backend.product.controller;

import org.example.cnjava_milkteastore.backend.product.service.ProductService;
import org.example.cnjava_milkteastore.backend.product.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping(params = "id")
    public ResponseEntity<ProductDTO> getById(@RequestParam int id) {
        return productService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductDTO> saveProduct(@RequestBody ProductDTO dto) {
        return ResponseEntity.ok(productService.createOrUpdate(dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(@RequestBody ProductDTO dto) {
        productService.delete(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-id")
    public ResponseEntity<Void> deleteById(@RequestParam int id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll() {
        productService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}