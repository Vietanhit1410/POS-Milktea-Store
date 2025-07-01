package org.example.cnjava_milkteastore.backend.employee.controller;

import org.example.cnjava_milkteastore.backend.employee.service.EmployeeService;
import org.example.cnjava_milkteastore.backend.employee.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    @GetMapping(params = "id")
    public ResponseEntity<EmployeeDTO> getById(@RequestParam int id) {
        return employeeService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> saveEmployee(@RequestBody EmployeeDTO dto) {
        return ResponseEntity.ok(employeeService.createOrUpdate(dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteEmployee(@RequestBody EmployeeDTO dto) {
        employeeService.delete(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-id")
    public ResponseEntity<Void> deleteById(@RequestParam int id) {
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll() {
        employeeService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}