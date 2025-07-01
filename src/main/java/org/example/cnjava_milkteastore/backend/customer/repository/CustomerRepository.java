package org.example.cnjava_milkteastore.backend.customer.repository;

import org.springframework.stereotype.Repository;
import org.example.cnjava_milkteastore.backend.customer.entity.Customer;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
}