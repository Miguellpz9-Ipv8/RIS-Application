package com.example.application.repositories;


import com.example.application.persistence.Order;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
 
public interface OrderRepository extends CrudRepository<Order, Long> {
    
    @Query("SELECT c FROM Order c where c.referral_md=:referral_md")
    public Iterable<Order> findAllOrdersByReferralmd(@Param("referral_md") Long referralMd);
    
    @Modifying
    @Query(value = "UPDATE orders SET appointment = ?1 WHERE order_id = ?2", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    int setAppointmentForOrder(Long appointment, Long id);
    
    @Modifying
    @Query(value = "UPDATE orders SET status = ?1 WHERE order_id = ?2", nativeQuery = true)
    @Transactional(rollbackFor = Exception.class)
    int setStatusForOrder(Long status, Long id);
}