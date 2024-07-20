package com.nt.red_distribute_api.service.imp;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.Util.DateTime;
import com.nt.red_distribute_api.config.AuthConfig;
import com.nt.red_distribute_api.dto.req.consumer.AddConsumerReq;
import com.nt.red_distribute_api.dto.req.consumer.UpdateByConsumerReq;
import com.nt.red_distribute_api.entity.ConsumerEntity;
import com.nt.red_distribute_api.repo.ConsumerRepo;
import com.nt.red_distribute_api.service.ConsumerService;

@Service
public class ConsumerImp implements ConsumerService {

    @Autowired
    private ConsumerRepo consumerRepo;

    @Autowired
    private AuthConfig authConfig;

    @Override
    public ConsumerEntity getConsumerByUsername(String username) {
        ConsumerEntity consumer = consumerRepo.ConsumerByUsername(username);

        return consumer;
    }

    @Override
    public Long registerConsumer(AddConsumerReq req, String createdBy) {
        Timestamp timeNow = DateTime.getTimeStampNow();
        String passwordEncode = authConfig.passwordEncoder().encode(req.getPassword());
        ConsumerEntity newConsumer = new ConsumerEntity();
        newConsumer.setUsername(req.getUsername());
        newConsumer.setSystem_name(req.getSystem_name());
        newConsumer.setPassword(passwordEncode);
        newConsumer.setConsumer_group(req.getSystem_name().toUpperCase());
        newConsumer.setDepartmentName(req.getDepartment_name());
        newConsumer.setContactName(req.getContact_name());
        newConsumer.setEmail(req.getEmail());
        newConsumer.setPhoneNumber(req.getPhone_number());
        newConsumer.setCreated_date(timeNow);
        newConsumer.setUpdated_date(timeNow);
        newConsumer.setUpdated_by(createdBy);
        newConsumer.setCreated_by(createdBy);
        
        ConsumerEntity created = consumerRepo.save(newConsumer);

        return created.getID()+1;
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

            if(updates.getConsumer_group()!= null){
                existingEntity.setConsumer_group(updates.getConsumer_group().toUpperCase());
            }

            if (updates.getEmail() != null ){
                existingEntity.setEmail(updates.getEmail());
            }

            if (updates.getPassword() != null ){
                String passwordEncode = authConfig.passwordEncoder().encode(updates.getPassword());
                existingEntity.setPassword(passwordEncode);
            }

            if (updates.getPhone_number() != null ){
                existingEntity.setPhoneNumber(updates.getPhone_number());
            }

            if (updates.getIs_enable() != null ){
                existingEntity.setIs_enable(updates.getIs_enable());
            }

            if (updates.getIs_delete() != null ){
                existingEntity.setIs_delete(updates.getIs_delete());
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
            consumerRepo.delete(existingEntity);
        }
    }

    @Override
    public ConsumerEntity consumerDetail(Long consumerID) {
        ConsumerEntity existingEntity = consumerRepo.findById(consumerID).orElse(null);
        return existingEntity;
    }
    

    

    
}
