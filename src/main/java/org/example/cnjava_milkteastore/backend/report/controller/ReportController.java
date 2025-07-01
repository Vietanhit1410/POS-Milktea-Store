package org.example.cnjava_milkteastore.backend.report.controller;

import org.example.cnjava_milkteastore.backend.report.service.ReportService;
import org.example.cnjava_milkteastore.backend.report.dto.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.getAll());
    }

    @GetMapping(params = "id")
    public ResponseEntity<ReportDTO> getById(@RequestParam int id) {
        return reportService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReportDTO> saveReport(@RequestBody ReportDTO dto) {
        return ResponseEntity.ok(reportService.createOrUpdate(dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteReport(@RequestBody ReportDTO dto) {
        reportService.delete(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-id")
    public ResponseEntity<Void> deleteById(@RequestParam int id) {
        reportService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll() {
        reportService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}