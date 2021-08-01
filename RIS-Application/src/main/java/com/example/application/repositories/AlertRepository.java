package com.example.application.repositories;


import com.example.application.persistence.Alert;

import org.springframework.data.repository.CrudRepository;
 
public interface AlertRepository extends CrudRepository<Alert, Long> {
 
}