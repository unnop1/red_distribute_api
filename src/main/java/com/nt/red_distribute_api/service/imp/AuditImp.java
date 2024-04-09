package com.nt.red_distribute_api.service.imp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.dto.resp.LoginResp;
import com.nt.red_distribute_api.entity.AuditEntity;
import com.nt.red_distribute_api.entity.UserEnitiy;
import com.nt.red_distribute_api.repo.AuditRepo;
import com.nt.red_distribute_api.repo.UserRepo;
import com.nt.red_distribute_api.service.AuditService;

@Service
public class AuditImp implements AuditService {

    @Autowired
    private AuditRepo auditRepo;

    @Override
    public List<AuditEntity> getAllAudit(Integer page, Integer limit) {
        // TODO Auto-generated method stub
        List<AuditEntity> auditEntities = auditRepo.findAll(page, limit);
        return auditEntities;
    }

    @Override
    public AuditEntity createAudit(AuditEntity AuditEntity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAudit'");
    }
    
}
