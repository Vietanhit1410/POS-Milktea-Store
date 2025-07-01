package org.example.cnjava_milkteastore.backend.report.repository;

import org.springframework.stereotype.Repository;
import org.example.cnjava_milkteastore.backend.report.entity.Report;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface ReportRepository extends CrudRepository<Report, Integer> {
}