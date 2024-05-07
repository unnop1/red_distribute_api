package com.nt.red_distribute_api.service;

import com.nt.red_distribute_api.dto.req.DefaultListReq;
import com.nt.red_distribute_api.dto.req.consumer.ListConsumerReq;
import com.nt.red_distribute_api.dto.req.manage_system.ListConsumerByOrderTypeReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;

public interface ManageSystemService {
    public PaginationDataResp ListManageOrderTypes(Integer page, Integer limit);
    public PaginationDataResp ListConsumerByOrderType(ListConsumerByOrderTypeReq req);
    public PaginationDataResp ListManageConsumers(ListConsumerReq req);
    public PaginationDataResp ListManageMetrics(DefaultListReq req);

}
