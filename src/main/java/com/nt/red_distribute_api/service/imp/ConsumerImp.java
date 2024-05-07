package com.nt.red_distribute_api.service.imp;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.Util.DateTime;
import com.nt.red_distribute_api.config.AuthConfig;
import com.nt.red_distribute_api.dto.req.consumer.AddConsumerReq;
import com.nt.red_distribute_api.dto.req.consumer.UpdateByConsumerReq;
import com.nt.red_distribute_api.entity.ConsumerEntity;
import com.nt.red_distribute_api.entity.OrderTypeEntity;
import com.nt.red_distribute_api.repo.ConsumerRepo;
import com.nt.red_distribute_api.service.ConsumerService;

@Service
public class ConsumerImp implements ConsumerService {

    @Autowired
    private ConsumerRepo consumerRepo;

    @Autowired
    private AuthConfig authConfig;

    @Override
    public Long registerConsumer(AddConsumerReq req, String createdBy) {
        Timestamp timeNow = DateTime.getTimeStampNow();
        String passwordEncode = authConfig.passwordEncoder().encode(req.getPassword());
        ConsumerEntity newConsumer = new ConsumerEntity();
        newConsumer.setUsername(req.getUsername());
        newConsumer.setSystem_name(req.getSystem_name());
        newConsumer.setPassword(passwordEncode);
        newConsumer.setDepartmentName(req.getDepartment_name());
        newConsumer.setContactName(req.getContact_name());
        newConsumer.setEmail(req.getEmail());
        newConsumer.setPhoneNumber(req.getPhone_number());
        newConsumer.setCreated_date(timeNow);
        newConsumer.setUpdated_date(timeNow);
        newConsumer.setUpdated_by(createdBy);
        newConsumer.setCreated_by(createdBy);
        
        ConsumerEntity created = consumerRepo.save(newConsumer);

        return created.getID();
    }

    @Override
    public ConsumerEntity updateConsumer(UpdateByConsumerReq req, String updatedBy) {
        ConsumerEntity existingEntity = consumerRepo.findById(req.getUpdateID()).orElse(null);
        
        if (existingEntity != null) {
            AddConsumerReq updates = req.getUpdateInfo();
            Timestamp timeNow = DateTime.getTimeStampNow();
            if (updates.getSystem_name() != null ){
                existingEntity.setSystem_name(updates.getSystem_name());
            }
            if (updates.getDepartment_name() != null ){
                existingEntity.setDepartmentName(updates.getDepartment_name());
            }
            if (updates.getContact_name() != null ){
                existingEntity.setContactName(updates.getContact_name());
            }

            if (updates.getEmail() != null ){
                existingEntity.setEmail(updates.getEmail());
            }

            if (updates.getPhone_number() != null ){
                existingEntity.setPhoneNumber(updates.getPhone_number());
            }

            if (updates.getIs_enable() != null ){
                existingEntity.setIs_enable(updates.getIs_enable());
            }

            if (updates.getIs_delete() == 0 ){
                existingEntity.setIs_delete(0);
            }

            existingEntity.setUpdated_by(updatedBy);
            existingEntity.setUpdated_date(timeNow);

            // Save the updated entity back to the database
            consumerRepo.save(existingEntity);
        }

        return existingEntity;

    }

    @Override
    public void deleteConsumer(Long consumerID, String deletedBy) {
        ConsumerEntity existingEntity = consumerRepo.findById(consumerID).orElse(null);
        
        if (existingEntity != null) {
            Timestamp timeNow = DateTime.getTimeStampNow();
            existingEntity.setIs_delete(1);
            existingEntity.setIs_delete_by(deletedBy);
            existingEntity.setIs_delete_date(timeNow);
            existingEntity.setUpdated_by(deletedBy);
            existingEntity.setUpdated_date(timeNow);

            // Save the updated entity back to the database
            consumerRepo.save(existingEntity);
        }
    }

    @Override
    public ConsumerEntity consumerDetail(Long consumerID) {
        ConsumerEntity existingEntity = consumerRepo.findById(consumerID).orElse(null);
        return existingEntity;
    }
    

    

    
}
