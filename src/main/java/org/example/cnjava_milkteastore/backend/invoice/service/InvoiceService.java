package org.example.cnjava_milkteastore.backend.invoice.service;

import org.example.cnjava_milkteastore.backend.invoice.dto.InvoiceDTO;
import org.example.cnjava_milkteastore.backend.invoice.entity.Invoice;
import org.example.cnjava_milkteastore.backend.invoice.repository.InvoiceRepository;
import org.example.cnjava_milkteastore.backend.invoice.mapper.InvoiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private InvoiceMapper invoiceMapper;

    @Transactional
    public InvoiceDTO createOrUpdate(InvoiceDTO dto) {
        Invoice entity;
        if (dto.getID() != 0) {
            entity = invoiceRepository.findById(dto.getID()).orElse(new Invoice());
            entity.setID(dto.getID());
            entity.setInvoiceCode(dto.getInvoiceCode());
            entity.setCustomerID(dto.getCustomerID());
            entity.setEmployeeID(dto.getEmployeeID());
            entity.setDateCreated(dto.getDateCreated());
            entity.setTotalPrice(dto.getTotalPrice());
            entity.setTotalQuantityDrink(dto.getTotalQuantityDrink());
            entity.setTotalQuantityFood(dto.getTotalQuantityFood());
            entity.setTotalPriceDrink(dto.getTotalPriceDrink());
            entity.setTotalPriceFood(dto.getTotalPriceFood());
        } else {
            entity = invoiceMapper.toEntity(dto);
        }
        Invoice saved = invoiceRepository.save(entity);
        return invoiceMapper.toDTO(saved);
    }

    public void delete(InvoiceDTO dto) {
        Invoice entity = invoiceMapper.toEntity(dto);
        invoiceRepository.delete(entity);
    }

    public void deleteById(int id) {
        invoiceRepository.deleteById(id);
    }

    public void deleteAll() {
        invoiceRepository.deleteAll();
    }

    public List<InvoiceDTO> getAll() {
        return ((List<Invoice>) invoiceRepository.findAll())
                .stream().map(invoiceMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<InvoiceDTO> getById(int id) {
        return invoiceRepository.findById(id).map(invoiceMapper::toDTO);
    }

}