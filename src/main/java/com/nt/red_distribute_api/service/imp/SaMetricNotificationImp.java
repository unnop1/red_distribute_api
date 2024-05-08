package com.nt.red_distribute_api.service.imp;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.Util.DateTime;
import com.nt.red_distribute_api.dto.req.sa_metric_notification.AddMetricNotificationReq;
import com.nt.red_distribute_api.dto.req.sa_metric_notification.UpdateMetricReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.entity.SaMetricNotificationEntity;
import com.nt.red_distribute_api.repo.SaMetricNotificationRepo;
import com.nt.red_distribute_api.service.SaMetricNotificationService;

@Service
public class SaMetricNotificationImp implements SaMetricNotificationService {

    @Autowired
    private SaMetricNotificationRepo saMetricNotificationRepo;

    @Override
    public Long registerMetricNotification(AddMetricNotificationReq req, String createdBy) {
        Timestamp timeNow = DateTime.getTimeStampNow();
        SaMetricNotificationEntity newMetric = new SaMetricNotificationEntity();
        newMetric.setEmail(req.getEmail());
        newMetric.setOM_NOT_CONNECT(req.getOM_NOT_CONNECT());
        newMetric.setDB_OM_NOT_CONNECT(req.getDB_OM_NOT_CONNECT());
        newMetric.setTOPUP_NOT_CONNECT(req.getTOPUP_NOT_CONNECT());
        newMetric.setTRIGGER_NOTI_JSON(req.getTRIGGER_NOTI_JSON());
        newMetric.setLINE_IS_ACTIVE(req.getLINE_IS_ACTIVE());
        newMetric.setLINE_TOKEN(req.getLINE_TOKEN());
        newMetric.setUPDATED_DATE(timeNow);
        newMetric.setUPDATED_By(createdBy);
        
        SaMetricNotificationEntity created = saMetricNotificationRepo.save(newMetric);

        return created.getID()+1;
    }

    @Override
    public SaMetricNotificationEntity updateMetricNotification(UpdateMetricReq req, String updatedBy) {
        SaMetricNotificationEntity existingEntity = saMetricNotificationRepo.findById(req.getUpdateID()).orElse(null);
        
        if (existingEntity != null) {
            AddMetricNotificationReq updates = req.getUpdateInfo();
            Timestamp timeNow = DateTime.getTimeStampNow();
            if (updates.getEmail() != null ){
                existingEntity.setEmail(updates.getEmail());
            }
            if (updates.getOM_NOT_CONNECT() != null ){
                existingEntity.setOM_NOT_CONNECT(updates.getOM_NOT_CONNECT());
            }
            if (updates.getDB_OM_NOT_CONNECT() != null ){
                existingEntity.setDB_OM_NOT_CONNECT(updates.getDB_OM_NOT_CONNECT());
            }

            if (updates.getTOPUP_NOT_CONNECT() != null ){
                existingEntity.setTOPUP_NOT_CONNECT(updates.getTOPUP_NOT_CONNECT());
            }

            if (updates.getTRIGGER_NOTI_JSON() != null ){
                existingEntity.setTRIGGER_NOTI_JSON(updates.getTRIGGER_NOTI_JSON());
            }

            if (updates.getLINE_IS_ACTIVE() != null ){
                existingEntity.setLINE_IS_ACTIVE(updates.getLINE_IS_ACTIVE());
            }

            if (updates.getLINE_TOKEN() != null ){
                existingEntity.setLINE_TOKEN(updates.getLINE_TOKEN());
            }

            existingEntity.setUPDATED_By(updatedBy);
            existingEntity.setUPDATED_DATE(timeNow);

            // Save the updated entity back to the database
            saMetricNotificationRepo.save(existingEntity);
        }
        
        return existingEntity;
    }

    @Override
    public SaMetricNotificationEntity saMetricNotificationDetail(Long saMetricID) {
        SaMetricNotificationEntity existingEntity = saMetricNotificationRepo.findById(saMetricID).orElse(null);
        return existingEntity;
    }

    
}
