package com.nt.red_distribute_api.service;

import java.util.List;

import com.nt.red_distribute_api.entity.AuditEntity;

public interface AuditService {
    List<AuditEntity> getAllAudit(Integer page, Integer limit);
    public AuditEntity createAudit(AuditEntity AuditEntity);
}
