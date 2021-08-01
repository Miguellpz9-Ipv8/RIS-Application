package com.example.application.repositories;


import com.example.application.persistence.Patient;

import org.springframework.data.repository.CrudRepository;
 
public interface PatientRepository extends CrudRepository<Patient, Long> {
 
    
}