package com.example.application.repositories;


import com.example.application.persistence.OrderStatus;

import org.springframework.data.repository.CrudRepository;
 
public interface OrderStatusRepository extends CrudRepository<OrderStatus, Long> {
 
}