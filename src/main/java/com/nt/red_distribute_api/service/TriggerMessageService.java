package com.nt.red_distribute_api.service;

import com.nt.red_distribute_api.dto.req.permission.AddPermissionReq;
import com.nt.red_distribute_api.dto.req.trigger.ListTriggerReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.entity.TriggerMessageEntity;



public interface TriggerMessageService  {
    public PaginationDataResp Dashboard(String sortBy, String sortName, String byType);
    public PaginationDataResp ListAllTrigger(ListTriggerReq req);
    public TriggerMessageEntity TriggerDetail(Long triggerID);
}
