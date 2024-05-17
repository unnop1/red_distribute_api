package com.nt.red_distribute_api.service;

import java.util.List;

import com.nt.red_distribute_api.dto.req.trigger.DashboardReq;
import com.nt.red_distribute_api.dto.req.trigger.ListTriggerReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.entity.TriggerMessageEntity;
import com.nt.red_distribute_api.entity.view.trigger.TriggerOrderTypeCount;



public interface TriggerMessageService  {
    public PaginationDataResp Dashboard(DashboardReq req);
    public PaginationDataResp ListAllTrigger(ListTriggerReq req);
    public TriggerMessageEntity TriggerDetail(Long triggerID);
    public List<TriggerOrderTypeCount> CountTriggerAllOrderType();
}
