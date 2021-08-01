package com.example.application.repositories;


import com.example.application.persistence.PatientsAlerts;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
 
public interface PatientsAlertsRepository extends CrudRepository<PatientsAlerts, Long> {
    
    @Transactional
    public void deleteByPatient(Long patient);

    @Query
    public Iterable<PatientsAlerts> findByPatient(Long patient);
}