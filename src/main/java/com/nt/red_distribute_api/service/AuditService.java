package com.nt.red_distribute_api.service;

import com.nt.red_distribute_api.enitiy.AuditEntity;


import java.util.List;

public interface AuditService {
    List<AuditEntity> getAllAudit(Integer page, Integer limit);
    public AuditEntity createAudit(AuditEntity AuditEntity);
}
