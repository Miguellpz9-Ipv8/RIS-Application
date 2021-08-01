package com.example.application.repositories;


import com.example.application.persistence.Appointment;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
 
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
    
    @Modifying
    @Query(value = "UPDATE appointments SET checked_in = 1 WHERE appointment_id = ?1", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    int setCheckedInForAppointment(Long appointment);
    
    @Modifying
    @Query(value = "UPDATE appointments SET closed = 1 WHERE appointment_id = ?1", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    int setClosedForAppointment(Long appointment);
    
}