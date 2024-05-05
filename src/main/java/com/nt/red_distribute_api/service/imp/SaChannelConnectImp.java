package com.nt.red_distribute_api.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.entity.LogLoginEntity;
import com.nt.red_distribute_api.entity.SaChannelConEntity;
import com.nt.red_distribute_api.repo.LogLoginRepo;
import com.nt.red_distribute_api.repo.SaChannelConnectRepo;
import com.nt.red_distribute_api.service.LogLoginService;
import com.nt.red_distribute_api.service.SaChannelConnectService;

@Service
public class SaChannelConnectImp implements SaChannelConnectService {

    @Autowired
    private SaChannelConnectRepo saChannelConnectRepo;

    @Override
    public PaginationDataResp ListChannelConnects() {
        PaginationDataResp resp = new PaginationDataResp();
        List<SaChannelConEntity> data = saChannelConnectRepo.findAll();
        resp.setCount(data.size());
        resp.setData(data);
        return resp;
    }

    // @Autowired
    // private LogLoginRepo logLoginRepo;
    
}
