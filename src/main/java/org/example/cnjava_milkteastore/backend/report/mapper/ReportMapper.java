package org.example.cnjava_milkteastore.backend.report.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.example.cnjava_milkteastore.backend.report.dto.ReportDTO;
import org.example.cnjava_milkteastore.backend.report.entity.Report;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    ReportDTO toDTO(Report entity);
    Report toEntity(ReportDTO dto);
}