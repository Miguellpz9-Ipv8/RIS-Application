package com.example.application.repositories;

import com.example.application.persistence.FileUpload;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
 
public interface FileUploadRepository extends CrudRepository<FileUpload, Long> {
 
    @Query(value = "SELECT * FROM file_uploads WHERE order_id = ?1", nativeQuery = true)
    public Iterable<FileUpload> getAllFileUploadsByOrderId(Long order);
}