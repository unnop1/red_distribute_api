package com.nt.red_distribute_api.service;

import com.nt.red_distribute_api.dto.req.DefaultListReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;

public interface NotificationMessageService  {
    public PaginationDataResp ListMetricNotificationMessages(DefaultListReq req);
}
