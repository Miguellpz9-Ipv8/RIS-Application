package com.example.application.repositories;

import com.example.application.persistence.DiagnosticReport;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
 
public interface DiagnosticRepository extends CrudRepository<DiagnosticReport, Long> {

    @Query("SELECT d FROM DiagnosticReport d WHERE d.order = :order")
    public DiagnosticReport getDiagnosticReportByOrderId(Long order);

    @Transactional
    public void deleteByOrder(Long order);
}