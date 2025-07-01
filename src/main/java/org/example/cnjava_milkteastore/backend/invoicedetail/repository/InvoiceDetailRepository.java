package org.example.cnjava_milkteastore.backend.invoicedetail.repository;

import org.springframework.stereotype.Repository;
import org.example.cnjava_milkteastore.backend.invoicedetail.entity.InvoiceDetail;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface InvoiceDetailRepository extends CrudRepository<InvoiceDetail, Integer> {
}