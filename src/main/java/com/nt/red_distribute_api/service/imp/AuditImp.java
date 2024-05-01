package com.nt.red_distribute_api.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.dto.req.audit.AuditLog;
import com.nt.red_distribute_api.dto.req.audit.ListAuditReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.entity.AuditLogEntity;
import com.nt.red_distribute_api.repo.AuditRepo;
import com.nt.red_distribute_api.service.AuditService;

@Service
public class AuditImp implements AuditService {
    @Autowired
    private AuditRepo auditRepo;

    @Override
    public PaginationDataResp ListAllAudit(ListAuditReq req) {
        PaginationDataResp pageResp = new PaginationDataResp();
        Integer offset = req.getStart();
        Integer limit = req.getLength();
        Integer page = 0;
        if (limit > 0){
            page = offset / limit;
        }
        String sortName = req.getSortName();
        String sortBy = req.getSortBy();
        System.out.println("sortName:" + sortName + " sortBy:" + sortBy);
        // String search = req.getSearch();
        // String searchField = req.getSearchField().toLowerCase();
        // Sort.Direction.fromString(sortBy), sortName );

        List<AuditLogEntity> resp = auditRepo.ListAllAudit(PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName ));
        Integer count = auditRepo.getTotalCount();
        
        pageResp.setCount(count);
        pageResp.setData(resp);
        return pageResp;
    }

    @Override
    public void AddAuditLog(AuditLog req) {
        System.out.println(req.toString());
        AuditLogEntity auditEntity = new AuditLogEntity();
        auditEntity.setAction(req.getAction());
        auditEntity.setAuditable(req.getAuditable());
        auditEntity.setUsername(req.getUsername());
        auditEntity.setBrowser(req.getBrowser());
        auditEntity.setDevice(req.getDevice());
        auditEntity.setIp_address(req.getIp_address());
        auditEntity.setOperating_system(req.getOperating_system());
        auditEntity.setAuditable_id(req.getAuditable_id());
        auditEntity.setComment(req.getComment());
        auditEntity.setCreated_date(req.getCreated_date());
        auditRepo.save(auditEntity);
        return;
    }

    
}
