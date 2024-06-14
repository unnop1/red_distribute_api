package com.nt.red_distribute_api.service.imp;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.Util.DateTime;
import com.nt.red_distribute_api.dto.req.ordertype.AddOrderTypeReq;
import com.nt.red_distribute_api.dto.req.ordertype.UpdateOrderTypeReq;
import com.nt.red_distribute_api.entity.OrderTypeEntity;
import com.nt.red_distribute_api.repo.OrderTypeRepo;
import com.nt.red_distribute_api.service.OrderTypeService;

@Service
public class OrderTypeImp implements OrderTypeService {

    @Autowired
    private OrderTypeRepo orderTypeRepo;

    @Override
    public List<OrderTypeEntity> ListAll() {
        List<OrderTypeEntity> listOrderTypeIDs = orderTypeRepo.findAll();
        return listOrderTypeIDs;
    }

    @Override
    public List<OrderTypeEntity> ListOrderTypeByIDs(List<Long> orderTypeIDs) {
        List<OrderTypeEntity> listOrderTypeIDs = orderTypeRepo.findAllById(orderTypeIDs);
        return listOrderTypeIDs;
    }

    @Override
    public OrderTypeEntity getOrderTypeByName(String orderTypeName) {
        OrderTypeEntity existsOrderType = orderTypeRepo.OrderTypeByName(orderTypeName);
        return existsOrderType;
    }

    @Override
    public Long registerOrderType(AddOrderTypeReq req, String createdBy) {
        Timestamp timeNow = DateTime.getTimeStampNow();
        OrderTypeEntity newOrderType = new OrderTypeEntity();
        newOrderType.setSA_CHANNEL_CONNECT_ID(req.getChannel_id());
        newOrderType.setOrderTypeName(req.getOrder_type_name().toUpperCase());
        newOrderType.setDESCRIPTION(req.getDescription());
        newOrderType.setMESSAGE_EXPIRE(req.getMessage_expire());
        newOrderType.setCreated_Date(timeNow);
        newOrderType.setUpdated_Date(timeNow);
        newOrderType.setUpdated_By(createdBy);
        newOrderType.setCreated_By(createdBy);
        
        OrderTypeEntity created = orderTypeRepo.save(newOrderType);

        return created.getID()+1;
    }

    @Override
    public OrderTypeEntity updateOrderType(UpdateOrderTypeReq req, String updatedBy) {
        OrderTypeEntity existingEntity = orderTypeRepo.findById(req.getUpdateID()).orElse(null);
        
        if (existingEntity != null) {
            AddOrderTypeReq updates = req.getUpdateInfo();
            Timestamp timeNow = DateTime.getTimeStampNow();
            if (updates.getChannel_id() != null ){
                existingEntity.setSA_CHANNEL_CONNECT_ID(updates.getChannel_id());
            }
            if (updates.getOrder_type_name() != null ){
                existingEntity.setOrderTypeName(updates.getOrder_type_name().toUpperCase());
            }
            if (updates.getDescription() != null ){
                existingEntity.setDESCRIPTION(updates.getDescription());
            }

            if (updates.getIs_delete() == 0 ){
                existingEntity.setIs_Delete(0);
            }

            if (updates.getMessage_expire() != null ){
                existingEntity.setMESSAGE_EXPIRE(updates.getMessage_expire());
            }

            if (updates.getIs_enable() != null ){
                existingEntity.setIs_Enable(updates.getIs_enable());
            }

            existingEntity.setUpdated_By(updatedBy);
            existingEntity.setUpdated_Date(timeNow);

            // Save the updated entity back to the database
            orderTypeRepo.save(existingEntity);
        }

        return existingEntity;
    }

    @Override
    public void deleteOrderType(Long orderTypeID, String deletedBy) {
        OrderTypeEntity existingEntity = orderTypeRepo.findById(orderTypeID).orElse(null);
        
        if (existingEntity != null) {
            Timestamp timeNow = DateTime.getTimeStampNow();
            existingEntity.setIs_Delete(1);
            existingEntity.setIs_Delete_By(deletedBy);
            existingEntity.setIs_Delete_Date(timeNow);
            existingEntity.setUpdated_By(deletedBy);
            existingEntity.setUpdated_Date(timeNow);

            // Save the updated entity back to the database
            orderTypeRepo.save(existingEntity);
        }
    }

    @Override
    public OrderTypeEntity getOrderTypeDetail(Long orderTypeID) {
        OrderTypeEntity existingEntity = orderTypeRepo.findById(orderTypeID).orElse(null);
        return existingEntity;
        
    }

    
    

    

    
}
