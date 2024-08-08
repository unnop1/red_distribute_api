package com.nt.red_distribute_api.service.imp;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.Util.DateTime;
import com.nt.red_distribute_api.config.AuthConfig;
import com.nt.red_distribute_api.entity.ConsumerOrderTypeEntity;
import com.nt.red_distribute_api.entity.view.consumer_ordertype.ConsumerLJoinOrderType;
import com.nt.red_distribute_api.repo.ConsumerOrderTypeRepo;
import com.nt.red_distribute_api.repo.ConsumerRepo;
import com.nt.red_distribute_api.service.ConsumerOrderTypeService;

@Service
public class ConsumerOrderTypeImp implements ConsumerOrderTypeService {

    @Autowired
    private ConsumerOrderTypeRepo consumerOrderTypeRepo;

    @Autowired
    private ConsumerRepo consumerRepo;

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
        consumerOrderTypeRepo.deleteConsumerOrderTypeByConsumerID(consumerID);
        for (Long updateOrderTypeID : updateOrderTypeIDs) {
            registerConsumerOrderType(consumerID, updateOrderTypeID, updatedBy);
        }
        
        return null;
    }

    @Override
    public Error upsertConsumerOrderType(Long consumerID, List<Long> updateOrderTypeIDs, String updatedBy) {
        
        for(Long updateOrderTypeID : updateOrderTypeIDs){
            ConsumerLJoinOrderType consumerJoinODT = consumerOrderTypeRepo.getConsumerOrderTypeByOrderTypeID(updateOrderTypeID);
            if(consumerJoinODT!=null){
                ConsumerOrderTypeEntity  consumerODT = consumerOrderTypeRepo.getConsumerOrderTypeByID(consumerJoinODT.getID());
                if(consumerODT == null){
                    ConsumerOrderTypeEntity newConsumerODT = new ConsumerOrderTypeEntity();
                    newConsumerODT.setConsumer_id(consumerID);
                    newConsumerODT.setCreated_by(updatedBy);
                    newConsumerODT.setOrdertype_id(updateOrderTypeID);
                    consumerOrderTypeRepo.save(newConsumerODT);
                }
            }
        }
        
        return null;
    }

    @Override
    public ConsumerLJoinOrderType FindOneConsumerOrderType(Long consumerID, Long orderTypeID) {
        ConsumerLJoinOrderType consumerJoinODT = consumerOrderTypeRepo.getOneConsumerOrderTypeUnique(consumerID, orderTypeID);
        return consumerJoinODT;
    }

    @Override
    public Error deleteConsumerOrderType(Long ID) {
        consumerOrderTypeRepo.deleteById(ID);
        return null;
    }

    @Override
    public Error deleteOrderTypeAllConsumer(Long orderTypeID) {
        consumerOrderTypeRepo.deleteConsumerOrderTypeByOrderTypeID(orderTypeID);
        return null;
    }

    

    

    
}
