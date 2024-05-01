package com.nt.red_distribute_api.service;

import com.nt.red_distribute_api.dto.req.audit.AuditLog;
import com.nt.red_distribute_api.dto.req.audit.ListAuditReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;

public interface AuditService {
    public PaginationDataResp ListAllAudit(ListAuditReq req);
    public void AddAuditLog(AuditLog req);
}
