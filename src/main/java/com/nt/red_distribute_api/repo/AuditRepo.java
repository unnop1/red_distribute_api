package com.nt.red_distribute_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nt.red_distribute_api.entity.AuditEntity;

import java.util.List;

public interface AuditRepo extends JpaRepository<AuditEntity,Long> {
    
    @Query(value = "SELECT * FROM audit LIMIT ?1, ?2", nativeQuery = true)
    public List<AuditEntity> findAll(Integer page, Integer Limit);

}
