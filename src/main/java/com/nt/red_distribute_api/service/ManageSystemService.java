package com.nt.red_distribute_api.service;

import com.nt.red_distribute_api.dto.req.DefaultListReq;
import com.nt.red_distribute_api.dto.req.manage_system.ListByOrderTypeReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;

public interface ManageSystemService {
    public PaginationDataResp ListManageOrderTypes(Integer page, Integer limit);
    public PaginationDataResp ListManageByOrderType(ListByOrderTypeReq req);
    public PaginationDataResp ListManageConsumers(DefaultListReq req);
    public PaginationDataResp ListManageMetrics(DefaultListReq req);

}
