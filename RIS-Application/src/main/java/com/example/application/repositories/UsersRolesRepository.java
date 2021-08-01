package com.example.application.repositories;


import com.example.application.persistence.UsersRoles;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
 
public interface UsersRolesRepository extends CrudRepository<UsersRoles, Long> {
    
    @Transactional
    public void deleteByUserid(Long user_id);
}