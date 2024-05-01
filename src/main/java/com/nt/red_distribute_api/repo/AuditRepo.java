package com.nt.red_distribute_api.repo;

    import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nt.red_distribute_api.entity.AuditLogEntity;
    
    public interface AuditRepo extends JpaRepository<AuditLogEntity,Long> {
        
        @Query(value = "SELECT * FROM audit_log",
                     nativeQuery = true)
        public List<AuditLogEntity> ListAllAudit(Pageable pageable);
    
        @Query(value = "SELECT  COUNT(*) FROM audit_log ", nativeQuery = true)
        public Integer getTotalCount();
    
    }