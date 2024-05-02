package com.nt.red_distribute_api.service.imp;

import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.dto.req.DefaultListReq;
import com.nt.red_distribute_api.dto.req.manage_system.ListByOrderTypeReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.service.ManageSystemService;

@Service
public class ManageSystemImp implements ManageSystemService{

    @Override
    public PaginationDataResp ListManageOrderTypes(Integer page, Integer limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ListManageOrderTypes'");
    }

    @Override
    public PaginationDataResp ListManageByOrderType(ListByOrderTypeReq req) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ListManageByOrderType'");
    }

    @Override
    public PaginationDataResp ListManageConsumers(DefaultListReq req) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ListManageConsumers'");
    }

    @Override
    public PaginationDataResp ListManageMetrics(DefaultListReq req) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ListManageMetrics'");
    }

}
