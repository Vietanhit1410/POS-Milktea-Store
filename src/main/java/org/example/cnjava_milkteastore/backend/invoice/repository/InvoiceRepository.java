package org.example.cnjava_milkteastore.backend.invoice.repository;

import org.springframework.stereotype.Repository;
import org.example.cnjava_milkteastore.backend.invoice.entity.Invoice;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Integer> {
}