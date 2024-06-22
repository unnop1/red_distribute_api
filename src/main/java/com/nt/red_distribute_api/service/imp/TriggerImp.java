package com.nt.red_distribute_api.service.imp;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.dto.req.trigger.DashboardReq;
import com.nt.red_distribute_api.dto.req.trigger.ListTriggerReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.entity.TriggerMessageEntity;
import com.nt.red_distribute_api.entity.view.order_type.OrderTypeDashboardTrigger;
import com.nt.red_distribute_api.entity.view.trigger.DashboardTrigger;
import com.nt.red_distribute_api.entity.view.trigger.TriggerOrderTypeCount;
import com.nt.red_distribute_api.repo.OrderTypeRepo;
import com.nt.red_distribute_api.repo.TriggerRepo;
import com.nt.red_distribute_api.service.TriggerMessageService;

@Service
public class TriggerImp implements TriggerMessageService{

    @Autowired
    private OrderTypeRepo orderTypeRepo;

    @Autowired
    private TriggerRepo triggerRepo;


    @Override
    public PaginationDataResp Dashboard(DashboardReq req) {
        PaginationDataResp resp = new PaginationDataResp();
        Timestamp startTime = Timestamp.valueOf(req.getStartTime());
        Timestamp endTime = Timestamp.valueOf(req.getEndTime());
        String sortName = req.getSortName();
        String sortBy = req.getSortBy();
        Long channelID = req.getChannelID();
        // System.out.println("startTime:" + startTime + " endTime:" + endTime);
        System.out.println("channelID:" + channelID );
        System.out.println("sortName:" + sortName + " sortBy:" + sortBy);
        if (channelID == 0){
            List<OrderTypeDashboardTrigger> dashboards = orderTypeRepo.OrderTypeTriggerDashboardByDate(startTime, endTime, PageRequest.of(0, 5000, Sort.Direction.fromString(sortBy), sortName));
            resp.setData(dashboards);
            resp.setCount(dashboards.size());
            return resp;
        }else{
            List<OrderTypeDashboardTrigger> dashboards = orderTypeRepo.OrderTypeTriggerDashboardByChannelIDAndDate(startTime, endTime, channelID, PageRequest.of(0, 5000, Sort.Direction.fromString(sortBy), sortName));
            resp.setData(dashboards);
            resp.setCount(dashboards.size());
            return resp;
        }
    }

    @Override
    public PaginationDataResp ListAllTrigger(ListTriggerReq req) {
        PaginationDataResp resp = new PaginationDataResp();
        Timestamp startTime = null;
        if (!req.getStartTime().isEmpty()){
            startTime = Timestamp.valueOf(req.getStartTime());
        }
        Timestamp endTime = null;
        if (!req.getEndTime().isEmpty()){
            endTime = Timestamp.valueOf(req.getEndTime());
        }
        Integer offset = req.getStart();
        Integer limit = req.getLength();
        Integer page = offset / limit;
        String sortName = req.getSortName();
        String sortBy = req.getSortBy();
        String search = req.getSearch();
        String searchField = req.getSearchField().toLowerCase();

        if ( search.isEmpty()){
            if (req.getStartTime().isEmpty() || req.getEndTime().isEmpty()){
                List<DashboardTrigger> smsConditionEntities = triggerRepo.ListTriggerWITHOUTTime(req.getOrderTypeID() ,PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName ));
                Integer count = triggerRepo.getListTriggerWITHOUTTimeTotalCount(req.getOrderTypeID());
                resp.setCount(count);
                resp.setData(smsConditionEntities);
                return resp;
            }
            List<DashboardTrigger> smsConditionEntities = triggerRepo.ListTriggerWithTime(req.getOrderTypeID(), startTime, endTime, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName ));
            Integer count = triggerRepo.getListTriggerWithTimeTotalCount(req.getOrderTypeID(), startTime, endTime);
            resp.setCount(count);
            resp.setData(smsConditionEntities);
            return resp;
        }else {
            if( !req.getSearchField().isEmpty()){
                if (searchField.equals("orderid")){
                    if (req.getStartTime().isEmpty() || req.getEndTime().isEmpty()){
                        List<DashboardTrigger> smsConditionEntities = triggerRepo.ListOrderIDWITHOUTTimeLike(req.getOrderTypeID(),search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                        Integer count = triggerRepo.getListOrderIDWITHOUTTimeLikeTotalCount(req.getOrderTypeID(),search);
                        resp.setCount(count);
                        resp.setData(smsConditionEntities);
                        return resp;
                    }
                    List<DashboardTrigger> smsConditionEntities = triggerRepo.ListOrderIDWithTimeLike(req.getOrderTypeID(),startTime, endTime, search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                    Integer count = triggerRepo.getListOrderIDWithTimeLikeTotalCount(req.getOrderTypeID(),startTime, endTime, search);
                    resp.setCount(count);
                    resp.setData(smsConditionEntities);
                    return resp;
                }else if (searchField.equals("phonenumber")){
                    if (req.getStartTime().isEmpty() || req.getEndTime().isEmpty()){
                        List<DashboardTrigger> smsConditionEntities = triggerRepo.ListPhoneNumberWITHOUTTimeLike(req.getOrderTypeID(),search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                        Integer count = triggerRepo.getListPhoneNumberWITHOUTTimeLikeTotalCount(req.getOrderTypeID(),search);
                        resp.setCount(count);
                        resp.setData(smsConditionEntities);
                        return resp;
                    }
                    List<DashboardTrigger> smsConditionEntities = triggerRepo.ListPhoneNumberWithTimeLike(req.getOrderTypeID(),startTime, endTime, search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                    Integer count = triggerRepo.getListPhoneNumberWithTimeLikeTotalCount(req.getOrderTypeID(),startTime, endTime, search);
                    resp.setCount(count);
                    resp.setData(smsConditionEntities);
                    return resp;
                }else if (searchField.equals("ordertype_name")){
                    if (req.getStartTime().isEmpty() || req.getEndTime().isEmpty()){
                        List<DashboardTrigger> smsConditionEntities = triggerRepo.ListORDERTYPENAMEWITHOUTTimeLike(req.getOrderTypeID(),search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                        Integer count = triggerRepo.getListORDERTYPENAMEWITHOUTTimeLikeTotalCount(req.getOrderTypeID(),search);
                        resp.setCount(count);
                        resp.setData(smsConditionEntities);
                        return resp;
                    }
                    List<DashboardTrigger> smsConditionEntities = triggerRepo.ListORDERTYPENAMEWithTimeLike(req.getOrderTypeID(),startTime, endTime, search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                    Integer count = triggerRepo.getListORDERTYPENAMEWithTimeLikeTotalCount(req.getOrderTypeID(),startTime, endTime, search);
                    resp.setCount(count);
                    resp.setData(smsConditionEntities);
                    return resp;
                } else if (searchField.equals("publish_channel")){
                    if (req.getStartTime().isEmpty() || req.getEndTime().isEmpty()){
                        List<DashboardTrigger> smsConditionEntities = triggerRepo.ListPUBLISHCHANNELWITHOUTTimeLike(req.getOrderTypeID(),search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                        Integer count = triggerRepo.getListPUBLISHCHANNELWITHOUTTimeLikeTotalCount(req.getOrderTypeID(),search);
                        resp.setCount(count);
                        resp.setData(smsConditionEntities);
                        return resp;
                    }
                    List<DashboardTrigger> smsConditionEntities = triggerRepo.ListORDERTYPENAMEWithTimeLike(req.getOrderTypeID(),startTime, endTime, search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                    Integer count = triggerRepo.getListORDERTYPENAMEWithTimeLikeTotalCount(req.getOrderTypeID(),startTime, endTime, search);
                    resp.setCount(count);
                    resp.setData(smsConditionEntities);
                    return resp;
                }
            }

            
            if (req.getStartTime().isEmpty() || req.getEndTime().isEmpty()){
                List<DashboardTrigger> smsConditionEntities = triggerRepo.ListTriggerWITHOUTTimeAllLike(req.getOrderTypeID(),search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                Integer count = triggerRepo.getListTriggerWITHOUTTimeAllLikeTotalCount(req.getOrderTypeID(), search);
                resp.setCount(count);
                resp.setData(smsConditionEntities);
                return resp;
            }
            List<DashboardTrigger> smsConditionEntities = triggerRepo.ListTriggerWithTimeAllLike(req.getOrderTypeID(),startTime, endTime, search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
            Integer count = triggerRepo.getListTriggerWithTimeAllLikeTotalCount(req.getOrderTypeID(),startTime, endTime, search);
            resp.setCount(count);
            resp.setData(smsConditionEntities);
            return resp;
        }
    }

    @Override
    public TriggerMessageEntity TriggerDetail(Long triggerID) {
        TriggerMessageEntity trigger = triggerRepo.findTriggerById(triggerID);
        return trigger;
    }

    @Override
    public List<TriggerOrderTypeCount> CountTriggerAllOrderType() {        
        List<TriggerOrderTypeCount> resp = orderTypeRepo.AllOrderTypeTriggerCount();
        return resp;
    }
    
}
