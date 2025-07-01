package org.example.cnjava_milkteastore.backend.customer.controller;

import org.example.cnjava_milkteastore.backend.customer.service.CustomerService;
import org.example.cnjava_milkteastore.backend.customer.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @GetMapping(params = "id")
    public ResponseEntity<CustomerDTO> getById(@RequestParam int id) {
        return customerService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> saveCustomer(@RequestBody CustomerDTO dto) {
        return ResponseEntity.ok(customerService.createOrUpdate(dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCustomer(@RequestBody CustomerDTO dto) {
        customerService.delete(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-id")
    public ResponseEntity<Void> deleteById(@RequestParam int id) {
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll() {
        customerService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}