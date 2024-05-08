package com.nt.red_distribute_api.service;

import java.util.List;

import com.nt.red_distribute_api.dto.req.ordertype.AddOrderTypeReq;
import com.nt.red_distribute_api.dto.req.ordertype.UpdateOrderTypeReq;
import com.nt.red_distribute_api.entity.OrderTypeEntity;



public interface OrderTypeService  {
    public Long registerOrderType(AddOrderTypeReq req, String createdBy);
    public OrderTypeEntity updateOrderType(UpdateOrderTypeReq req, String updatedBy);
    public void deleteOrderType(Long orderTypeID, String deletedBy);
    public OrderTypeEntity getOrderTypeDetail(Long orderTypeID);
    public OrderTypeEntity getOrderTypeByName(String orderTypeName);
    public List<OrderTypeEntity> ListOrderTypeByIDs(List<Long> orderTypeIDs);
}
