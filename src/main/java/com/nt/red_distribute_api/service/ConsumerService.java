package com.nt.red_distribute_api.service;

import com.nt.red_distribute_api.dto.req.consumer.AddConsumerReq;
import com.nt.red_distribute_api.dto.req.consumer.UpdateByConsumerReq;
import com.nt.red_distribute_api.entity.ConsumerEntity;



public interface ConsumerService  {
    public Long registerConsumer(AddConsumerReq req, String createdBy);
    public ConsumerEntity updateConsumer(UpdateByConsumerReq req, String updatedBy);
    public void deleteConsumer(Long consumerID, String updatedBy);
    public ConsumerEntity consumerDetail(Long consumerID);
}
