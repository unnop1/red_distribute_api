package com.nt.red_distribute_api.service.imp;

import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.dto.req.consumer.AddConsumerReq;
import com.nt.red_distribute_api.dto.req.consumer.UpdateByConsumerReq;
import com.nt.red_distribute_api.entity.ConsumerEntity;
import com.nt.red_distribute_api.service.ConsumerService;

@Service
public class ConsumerImp implements ConsumerService {

    @Override
    public Long registerConsumer(AddConsumerReq req, String createdBy) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerConsumer'");
    }

    @Override
    public void updateConsumer(UpdateByConsumerReq req, String updatedBy) {
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
