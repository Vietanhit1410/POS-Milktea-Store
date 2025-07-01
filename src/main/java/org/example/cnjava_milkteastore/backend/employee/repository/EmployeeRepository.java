package org.example.cnjava_milkteastore.backend.employee.repository;

import org.springframework.stereotype.Repository;
import org.example.cnjava_milkteastore.backend.employee.entity.Employee;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
}