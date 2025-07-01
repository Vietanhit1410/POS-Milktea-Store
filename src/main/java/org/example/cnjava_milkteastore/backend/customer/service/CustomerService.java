package org.example.cnjava_milkteastore.backend.customer.service;

import org.example.cnjava_milkteastore.backend.customer.dto.CustomerDTO;
import org.example.cnjava_milkteastore.backend.customer.entity.Customer;
import org.example.cnjava_milkteastore.backend.customer.repository.CustomerRepository;
import org.example.cnjava_milkteastore.backend.customer.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;

    @Transactional
    public CustomerDTO createOrUpdate(CustomerDTO dto) {
        Customer entity;
        if (dto.getID() != 0) {
            entity = customerRepository.findById(dto.getID()).orElse(new Customer());
            entity.setID(dto.getID());
            entity.setCustomerCode(dto.getCustomerCode());
            entity.setCustomerName(dto.getCustomerName());
            entity.setCustomerPhone(dto.getCustomerPhone());
            entity.setPoints(dto.getPoints());
        } else {
            entity = customerMapper.toEntity(dto);
        }
        Customer saved = customerRepository.save(entity);
        return customerMapper.toDTO(saved);
    }

    public void delete(CustomerDTO dto) {
        Customer entity = customerMapper.toEntity(dto);
        customerRepository.delete(entity);
    }

    public void deleteById(int id) {
        customerRepository.deleteById(id);
    }

    public void deleteAll() {
        customerRepository.deleteAll();
    }

    public List<CustomerDTO> getAll() {
        return ((List<Customer>) customerRepository.findAll())
                .stream().map(customerMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<CustomerDTO> getById(int id) {
        return customerRepository.findById(id).map(customerMapper::toDTO);
    }

}