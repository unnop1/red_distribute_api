package com.nt.red_distribute_api.service.imp;

import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.dto.req.trigger.ListTriggerReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.entity.TriggerMessageEntity;
import com.nt.red_distribute_api.service.TriggerMessageService;

@Service
public class TriggerImp implements TriggerMessageService{

    @Override
    public PaginationDataResp Dashboard(String sortBy, String sortName, String byType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Dashboard'");
    }

    @Override
    public PaginationDataResp ListAllTrigger(ListTriggerReq req) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ListAllTrigger'");
    }

    @Override
    public TriggerMessageEntity TriggerDetail(Long triggerID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'TriggerDetail'");
    }
    
}
