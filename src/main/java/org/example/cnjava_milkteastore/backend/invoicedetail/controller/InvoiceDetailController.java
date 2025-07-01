package org.example.cnjava_milkteastore.backend.invoicedetail.controller;

import org.example.cnjava_milkteastore.backend.invoicedetail.service.InvoiceDetailService;
import org.example.cnjava_milkteastore.backend.invoicedetail.dto.InvoiceDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/invoicedetails")
public class InvoiceDetailController {

    @Autowired
    private InvoiceDetailService invoiceDetailService;

    @GetMapping
    public ResponseEntity<List<InvoiceDetailDTO>> getAllInvoiceDetails() {
        return ResponseEntity.ok(invoiceDetailService.getAll());
    }

    @GetMapping(params = "id")
    public ResponseEntity<InvoiceDetailDTO> getById(@RequestParam int id) {
        return invoiceDetailService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InvoiceDetailDTO> saveInvoiceDetail(@RequestBody InvoiceDetailDTO dto) {
        return ResponseEntity.ok(invoiceDetailService.createOrUpdate(dto));
    }
    @PostMapping("/all")
    public ResponseEntity<List<InvoiceDetailDTO>> saveInvoiceDetail(@RequestBody List<InvoiceDetailDTO> dto) {
        for (InvoiceDetailDTO d : dto) {
            invoiceDetailService.createOrUpdate(d);
        }
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteInvoiceDetail(@RequestBody InvoiceDetailDTO dto) {
        invoiceDetailService.delete(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-id")
    public ResponseEntity<Void> deleteById(@RequestParam int id) {
        invoiceDetailService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll() {
        invoiceDetailService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}