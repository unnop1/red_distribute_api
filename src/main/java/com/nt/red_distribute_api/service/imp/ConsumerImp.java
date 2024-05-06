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
        newConsumer.setCreated_by(createdBy);
        
        ConsumerEntity created = consumerRepo.save(newConsumer);

        return created.getID();
    }

    @Override
    public ConsumerEntity updateConsumer(UpdateByConsumerReq req, String updatedBy) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateConsumer'");
    }

    @Override
    public void deleteConsumer(Long consumerID, String updatedBy) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteConsumer'");
    }

    @Override
    public ConsumerEntity consumerDetail(Long consumerID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'consumerDetail'");
    }
    

    

    
}
