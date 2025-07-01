package org.example.cnjava_milkteastore.backend.invoicedetail.service;

import org.example.cnjava_milkteastore.backend.invoicedetail.dto.InvoiceDetailDTO;
import org.example.cnjava_milkteastore.backend.invoicedetail.entity.InvoiceDetail;
import org.example.cnjava_milkteastore.backend.invoicedetail.repository.InvoiceDetailRepository;
import org.example.cnjava_milkteastore.backend.invoicedetail.mapper.InvoiceDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceDetailService {

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;
    @Autowired
    private InvoiceDetailMapper invoiceDetailMapper;

    @Transactional
    public InvoiceDetailDTO createOrUpdate(InvoiceDetailDTO dto) {
        InvoiceDetail entity;
        if (dto.getID() != 0) {
            entity = invoiceDetailRepository.findById(dto.getID()).orElse(new InvoiceDetail());
            entity.setID(dto.getID());
            entity.setInvoiceID(dto.getInvoiceID());
            entity.setProductID(dto.getProductID());
            entity.setQuantity(dto.getQuantity());
            entity.setPrice(dto.getPrice());
            entity.setProductName(dto.getProductName());
            entity.setProductType(dto.getProductType());
        } else {
            entity = invoiceDetailMapper.toEntity(dto);
        }
        InvoiceDetail saved = invoiceDetailRepository.save(entity);
        return invoiceDetailMapper.toDTO(saved);
    }

    public void delete(InvoiceDetailDTO dto) {
        InvoiceDetail entity = invoiceDetailMapper.toEntity(dto);
        invoiceDetailRepository.delete(entity);
    }

    public void deleteById(int id) {
        invoiceDetailRepository.deleteById(id);
    }

    public void deleteAll() {
        invoiceDetailRepository.deleteAll();
    }

    public List<InvoiceDetailDTO> getAll() {
        return ((List<InvoiceDetail>) invoiceDetailRepository.findAll())
                .stream().map(invoiceDetailMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<InvoiceDetailDTO> getById(int id) {
        return invoiceDetailRepository.findById(id).map(invoiceDetailMapper::toDTO);
    }

}