package org.example.cnjava_milkteastore.backend.invoice.controller;

import org.example.cnjava_milkteastore.backend.invoice.service.InvoiceService;
import org.example.cnjava_milkteastore.backend.invoice.dto.InvoiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAll());
    }

    @GetMapping(params = "id")
    public ResponseEntity<InvoiceDTO> getById(@RequestParam int id) {
        return invoiceService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InvoiceDTO> saveInvoice(@RequestBody InvoiceDTO dto) {
        return ResponseEntity.ok(invoiceService.createOrUpdate(dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteInvoice(@RequestBody InvoiceDTO dto) {
        invoiceService.delete(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-id")
    public ResponseEntity<Void> deleteById(@RequestParam int id) {
        invoiceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll() {
        invoiceService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}