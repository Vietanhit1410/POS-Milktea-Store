package org.example.cnjava_milkteastore.backend.employee.service;

import org.example.cnjava_milkteastore.backend.employee.dto.EmployeeDTO;
import org.example.cnjava_milkteastore.backend.employee.entity.Employee;
import org.example.cnjava_milkteastore.backend.employee.repository.EmployeeRepository;
import org.example.cnjava_milkteastore.backend.employee.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeMapper employeeMapper;

    @Transactional
    public EmployeeDTO createOrUpdate(EmployeeDTO dto) {
        Employee entity;
        if (dto.getID() != 0) {
            entity = employeeRepository.findById(dto.getID()).orElse(new Employee());
            entity.setID(dto.getID());
            entity.setEmployeeCode(dto.getEmployeeCode());
            entity.setName(dto.getName());
            entity.setAddress(dto.getAddress());
            entity.setGender(dto.getGender());
            entity.setBirthDate(dto.getBirthDate());
            entity.setPhone(dto.getPhone());
        } else {
            entity = employeeMapper.toEntity(dto);
        }
        Employee saved = employeeRepository.save(entity);
        return employeeMapper.toDTO(saved);
    }

    public void delete(EmployeeDTO dto) {
        Employee entity = employeeMapper.toEntity(dto);
        employeeRepository.delete(entity);
    }

    public void deleteById(int id) {
        employeeRepository.deleteById(id);
    }

    public void deleteAll() {
        employeeRepository.deleteAll();
    }

    public List<EmployeeDTO> getAll() {
        return ((List<Employee>) employeeRepository.findAll())
                .stream().map(employeeMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<EmployeeDTO> getById(int id) {
        return employeeRepository.findById(id).map(employeeMapper::toDTO);
    }

}