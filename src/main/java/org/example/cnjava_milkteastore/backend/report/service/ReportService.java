package org.example.cnjava_milkteastore.backend.report.service;

import org.example.cnjava_milkteastore.backend.report.dto.ReportDTO;
import org.example.cnjava_milkteastore.backend.report.entity.Report;
import org.example.cnjava_milkteastore.backend.report.repository.ReportRepository;
import org.example.cnjava_milkteastore.backend.report.mapper.ReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ReportMapper reportMapper;

    @Transactional
    public ReportDTO createOrUpdate(ReportDTO dto) {
        Report entity;
        if (dto.getID() != 0) {
            entity = reportRepository.findById(dto.getID()).orElse(new Report());
            entity.setID(dto.getID());
            entity.setReportCode(dto.getReportCode());
            entity.setCreateAt(dto.getCreateAt());
            entity.setTotalPrice(dto.getTotalPrice());
            entity.setTotalProductSold(dto.getTotalProductSold());
            entity.setTotalProductFood(dto.getTotalProductFood());
            entity.setTotalProductDrink(dto.getTotalProductDrink());
            entity.setQuantityDrink(dto.getQuantityDrink());
        } else {
            entity = reportMapper.toEntity(dto);
        }
        Report saved = reportRepository.save(entity);
        return reportMapper.toDTO(saved);
    }

    public void delete(ReportDTO dto) {
        Report entity = reportMapper.toEntity(dto);
        reportRepository.delete(entity);
    }

    public void deleteById(int id) {
        reportRepository.deleteById(id);
    }

    public void deleteAll() {
        reportRepository.deleteAll();
    }

    public List<ReportDTO> getAll() {
        return ((List<Report>) reportRepository.findAll())
                .stream().map(reportMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<ReportDTO> getById(int id) {
        return reportRepository.findById(id).map(reportMapper::toDTO);
    }

}