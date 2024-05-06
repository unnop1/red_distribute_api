package com.nt.red_distribute_api.service;

import java.util.List;

import com.nt.red_distribute_api.entity.view.consumer_ordertype.ConsumerLJoinOrderType;



public interface ConsumerOrderTypeService  {
    public List<ConsumerLJoinOrderType> ListConsumerOrderType(Long consumerID);
}
