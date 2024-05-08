package com.nt.red_distribute_api.service;

import java.util.List;

import com.nt.red_distribute_api.dto.req.sa_metric_notification.AddMetricNotificationReq;
import com.nt.red_distribute_api.dto.req.sa_metric_notification.UpdateMetricReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.entity.SaMetricNotificationEntity;

public interface SaMetricNotificationService {
    public Long registerMetricNotification(AddMetricNotificationReq req, String createdBy);
    public SaMetricNotificationEntity updateMetricNotification(UpdateMetricReq req, String createdBy);
    public SaMetricNotificationEntity saMetricNotificationDetail(Long saMetricID);
}
