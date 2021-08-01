package com.example.application.repositories;


import com.example.application.persistence.Role;

import org.springframework.data.repository.CrudRepository;
 
public interface RoleRepository extends CrudRepository<Role, Long> {
 
}