package com.nt.red_distribute_api.service;

import java.util.List;

import com.nt.red_distribute_api.entity.view.consumer_ordertype.ConsumerLJoinOrderType;



public interface ConsumerOrderTypeService  {
    public List<ConsumerLJoinOrderType> ListConsumerOrderType(Long consumerID);
    public ConsumerLJoinOrderType FindOneConsumerOrderType(Long consumerID, Long orderTypeID);
    public Long registerConsumerOrderType(Long consumerID, Long orderTypeID, String createdBy);
    public Error updateConsumerOrderType(Long consumerID, List<Long> orderTypeIDs, String updatedBy);
    public Error upsertConsumerOrderType(Long consumerID, List<Long> orderTypeIDs, String updatedBy);
    public Error deleteConsumerOrderType(Long ID);
    public Error deleteOrderTypeAllConsumer(Long ordertypeID);
}
