package com.nt.red_distribute_api.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.entity.LogLoginEntity;
import com.nt.red_distribute_api.repo.LogLoginRepo;
import com.nt.red_distribute_api.service.LogLoginService;

@Service
public class LogLogin implements LogLoginService {

    @Autowired
    private LogLoginRepo logLoginRepo;

    @Override
    public void createLog(LogLoginEntity log) {
        logLoginRepo.save(log);
    }
    
}
