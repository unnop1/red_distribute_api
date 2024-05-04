package com.nt.red_distribute_api.service.imp;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.dto.req.trigger.ListTriggerReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.entity.TriggerMessageEntity;
import com.nt.red_distribute_api.entity.view.order_type.OrderTypeDashboardTrigger;
import com.nt.red_distribute_api.entity.view.trigger.DashboardTrigger;
import com.nt.red_distribute_api.repo.OrderTypeRepo;
import com.nt.red_distribute_api.repo.TriggerRepo;
import com.nt.red_distribute_api.service.TriggerMessageService;
import com.nt.red_distribute_api.dto.req.trigger.DashboardReq;

@Service
public class TriggerImp implements TriggerMessageService{

    @Autowired
    private OrderTypeRepo orderTypeRepo;

    @Autowired
    private TriggerRepo triggerRepo;


    @Override
    public PaginationDataResp Dashboard(DashboardReq req) {
        PaginationDataResp resp = new PaginationDataResp();
        // Timestamp startTime = Timestamp.valueOf(req.getStartTime());
        // Timestamp endTime = Timestamp.valueOf(req.getEndTime());
        String sortName = req.getSortName();
        String sortBy = req.getSortBy();
        String byType = req.getByType();
        // System.out.println("startTime:" + startTime + " endTime:" + endTime);
        System.out.println("sortName:" + sortName + " sortBy:" + sortBy);
        if (byType.equals("all")){
            List<OrderTypeDashboardTrigger> dashboards = orderTypeRepo.OrderTypeTriggerDashboard(PageRequest.of(0, 5000, Sort.Direction.fromString(sortBy), sortName));
            resp.setData(dashboards);
            resp.setCount(dashboards.size());
            return resp;
        }else{
            List<OrderTypeDashboardTrigger> dashboards = orderTypeRepo.OrderTypeTriggerDashboard(PageRequest.of(0, 5000, Sort.Direction.fromString(sortBy), sortName));
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
                List<DashboardTrigger> smsConditionEntities = triggerRepo.ListTriggerWITHOUTTime(PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName ));
                Integer count = triggerRepo.getListTriggerWITHOUTTimeTotalCount();
                resp.setCount(count);
                resp.setData(smsConditionEntities);
                return resp;
            }
            List<DashboardTrigger> smsConditionEntities = triggerRepo.ListTriggerWithTime(startTime, endTime, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName ));
            Integer count = triggerRepo.getListTriggerWithTimeTotalCount(startTime, endTime);
            resp.setCount(count);
            resp.setData(smsConditionEntities);
            return resp;
        }else {
            if( !req.getSearchField().isEmpty()){
                if (searchField.equals("orderid")){
                    if (req.getStartTime().isEmpty() || req.getEndTime().isEmpty()){
                        List<DashboardTrigger> smsConditionEntities = triggerRepo.ListOrderIDWITHOUTTimeLike(search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                        Integer count = triggerRepo.getListOrderIDWITHOUTTimeLikeTotalCount(search);
                        resp.setCount(count);
                        resp.setData(smsConditionEntities);
                        return resp;
                    }
                    List<DashboardTrigger> smsConditionEntities = triggerRepo.ListOrderIDWithTimeLike(startTime, endTime, search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                    Integer count = triggerRepo.getListOrderIDWithTimeLikeTotalCount(startTime, endTime, search);
                    resp.setCount(count);
                    resp.setData(smsConditionEntities);
                    return resp;
                }else if (searchField.equals("phonenumber")){
                    if (req.getStartTime().isEmpty() || req.getEndTime().isEmpty()){
                        List<DashboardTrigger> smsConditionEntities = triggerRepo.ListPhoneNumberWITHOUTTimeLike(search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                        Integer count = triggerRepo.getListPhoneNumberWITHOUTTimeLikeTotalCount(search);
                        resp.setCount(count);
                        resp.setData(smsConditionEntities);
                        return resp;
                    }
                    List<DashboardTrigger> smsConditionEntities = triggerRepo.ListPhoneNumberWithTimeLike(startTime, endTime, search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                    Integer count = triggerRepo.getListPhoneNumberWithTimeLikeTotalCount(startTime, endTime, search);
                    resp.setCount(count);
                    resp.setData(smsConditionEntities);
                    return resp;
                }else if (searchField.equals("ordertype_name")){
                    if (req.getStartTime().isEmpty() || req.getEndTime().isEmpty()){
                        List<DashboardTrigger> smsConditionEntities = triggerRepo.ListORDERTYPENAMEWITHOUTTimeLike(search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                        Integer count = triggerRepo.getListORDERTYPENAMEWITHOUTTimeLikeTotalCount(search);
                        resp.setCount(count);
                        resp.setData(smsConditionEntities);
                        return resp;
                    }
                    List<DashboardTrigger> smsConditionEntities = triggerRepo.ListORDERTYPENAMEWithTimeLike(startTime, endTime, search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                    Integer count = triggerRepo.getListORDERTYPENAMEWithTimeLikeTotalCount(startTime, endTime, search);
                    resp.setCount(count);
                    resp.setData(smsConditionEntities);
                    return resp;
                } else if (searchField.equals("publish_channel")){
                    if (req.getStartTime().isEmpty() || req.getEndTime().isEmpty()){
                        List<DashboardTrigger> smsConditionEntities = triggerRepo.ListPUBLISHCHANNELWITHOUTTimeLike(search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                        Integer count = triggerRepo.getListPUBLISHCHANNELWITHOUTTimeLikeTotalCount(search);
                        resp.setCount(count);
                        resp.setData(smsConditionEntities);
                        return resp;
                    }
                    List<DashboardTrigger> smsConditionEntities = triggerRepo.ListORDERTYPENAMEWithTimeLike(startTime, endTime, search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                    Integer count = triggerRepo.getListORDERTYPENAMEWithTimeLikeTotalCount(startTime, endTime, search);
                    resp.setCount(count);
                    resp.setData(smsConditionEntities);
                    return resp;
                }
            }

            
            if (req.getStartTime().isEmpty() || req.getEndTime().isEmpty()){
                List<DashboardTrigger> smsConditionEntities = triggerRepo.ListTriggerWITHOUTTimeAllLike(search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
                Integer count = triggerRepo.getListTriggerWITHOUTTimeAllLikeTotalCount(search);
                resp.setCount(count);
                resp.setData(smsConditionEntities);
                return resp;
            }
            List<DashboardTrigger> smsConditionEntities = triggerRepo.ListTriggerWithTimeAllLike(startTime, endTime, search, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
            Integer count = triggerRepo.getListTriggerWithTimeAllLikeTotalCount(startTime, endTime, search);
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
    
}
