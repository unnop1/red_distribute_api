package com.nt.red_distribute_api.service.imp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.Util.DateTime;
import com.nt.red_distribute_api.config.AuthConfig;
import com.nt.red_distribute_api.dto.req.consumer.AddConsumerReq;
import com.nt.red_distribute_api.dto.req.consumer.UpdateByConsumerReq;
import com.nt.red_distribute_api.entity.ConsumerEntity;
import com.nt.red_distribute_api.entity.ConsumerOrderTypeEntity;
import com.nt.red_distribute_api.entity.view.consumer_ordertype.ConsumerLJoinOrderType;
import com.nt.red_distribute_api.repo.ConsumerOrderTypeRepo;
import com.nt.red_distribute_api.repo.ConsumerRepo;
import com.nt.red_distribute_api.service.ConsumerOrderTypeService;
import com.nt.red_distribute_api.service.ConsumerService;

@Service
public class ConsumerOrderTypeImp implements ConsumerOrderTypeService {

    @Autowired
    private ConsumerOrderTypeRepo consumerOrderTypeRepo;

    @Autowired
    private AuthConfig authConfig;

    @Override
    public List<ConsumerLJoinOrderType> ListConsumerOrderType(Long consumerID) {
        List<ConsumerLJoinOrderType> consumerOdts = consumerOrderTypeRepo.ConsumerOrderTypeName(consumerID);
        return consumerOdts;
    }

    @Override
    public Long registerConsumerOrderType(Long consumerID, Long orderTypeID, String createdBy) {
        Timestamp timeNow = DateTime.getTimeStampNow();
        ConsumerOrderTypeEntity newConsumerOrderType = new ConsumerOrderTypeEntity();
        newConsumerOrderType.setConsumer_id(consumerID);
        newConsumerOrderType.setOrdertype_id(orderTypeID);
        newConsumerOrderType.setCreated_date(timeNow);
        newConsumerOrderType.setCreated_by(createdBy);
        
        ConsumerOrderTypeEntity created = consumerOrderTypeRepo.save(newConsumerOrderType);

        return created.getID()+1;
    }

    @Override
    public Error updateConsumerOrderType(Long consumerID, List<Long> updateOrderTypeIDs, String updatedBy) {
        List<ConsumerLJoinOrderType> consumerOdts = consumerOrderTypeRepo.ConsumerOrderTypeName(consumerID);
        List<Long> deleteOlds = new ArrayList<Long>();
        for (ConsumerLJoinOrderType consumerOdtOld : consumerOdts) {
            deleteOlds.add(consumerOdtOld.getID());
        }
        consumerOrderTypeRepo.deleteConsumerOrderTypeByConsumerID(consumerID);
        for (Long updateOrderTypeID : updateOrderTypeIDs) {
            registerConsumerOrderType(consumerID, updateOrderTypeID, updatedBy);
        }
        
        return null;
    }

    

    

    
}
