package com.nt.red_distribute_api.service.imp;

import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.dto.req.sa_metric_notification.AddMetricNotificationReq;
import com.nt.red_distribute_api.dto.req.sa_metric_notification.UpdateMetricReq;
import com.nt.red_distribute_api.entity.SaMetricNotificationEntity;
import com.nt.red_distribute_api.service.SaMetricNotificationService;

@Service
public class SaMetricNotificationImp implements SaMetricNotificationService {

    @Override
    public Long registerMetricNotification(AddMetricNotificationReq req, String createdBy) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerMetricNotification'");
    }

    @Override
    public void updateMetricNotification(UpdateMetricReq req, String createdBy) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateMetricNotification'");
    }

    @Override
    public SaMetricNotificationEntity saMetricNotificationDetail(Long saMetricID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saMetricNotificationDetail'");
    }

    
}
