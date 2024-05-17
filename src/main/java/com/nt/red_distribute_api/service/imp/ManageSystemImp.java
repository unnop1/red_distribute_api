package com.nt.red_distribute_api.service.imp;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.dto.req.DefaultListReq;
import com.nt.red_distribute_api.dto.req.consumer.ListConsumerReq;
import com.nt.red_distribute_api.dto.req.manage_system.ListConsumerByOrderTypeReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.entity.SaMetricNotificationEntity;
import com.nt.red_distribute_api.entity.view.consumer.ListConsumerTopic;
import com.nt.red_distribute_api.entity.view.order_type.OrderTypeDashboard;
import com.nt.red_distribute_api.entity.view.order_type.OrderTypeDashboardTrigger;
import com.nt.red_distribute_api.entity.view.trigger.DashboardTrigger;
import com.nt.red_distribute_api.repo.ConsumerRepo;
import com.nt.red_distribute_api.repo.OrderTypeRepo;
import com.nt.red_distribute_api.repo.SaMetricNotificationRepo;
import com.nt.red_distribute_api.service.ManageSystemService;

@Service
public class ManageSystemImp implements ManageSystemService{
    @Autowired
    private OrderTypeRepo orderTypeRepo;

    @Autowired
    private SaMetricNotificationRepo saMetricNotificationRepo;

    @Autowired
    private ConsumerRepo consumerRepo;

    @Override
    public PaginationDataResp ListManageOrderTypes(Integer page, Integer limit) {
        PaginationDataResp resp = new PaginationDataResp();
        Integer offset = (page - 1) * limit;
        List<OrderTypeDashboard> data = orderTypeRepo.ListManageOrderType(offset, limit);
        Integer count = orderTypeRepo.getListManageOrderTypeTotal();
        resp.setCount(count);
        resp.setData(data);
        return resp;
    }

    @Override
    public PaginationDataResp ListConsumerByOrderType(ListConsumerByOrderTypeReq req) {
        PaginationDataResp resp = new PaginationDataResp();
        Integer offset = req.getStart();
        Integer limit = req.getLength();
        Integer page = offset / limit;
        String sortName = req.getSortName();
        Long orderTypeID = req.getOrderTypeID();
        String sortBy = req.getSortBy();
        String search = req.getSearch();
        JpaSort sort = JpaSort.unsafe(Sort.Direction.fromString(sortBy), "( con."+sortName+")");
        String searchField = req.getSearchField().toLowerCase();

        if ( search.isEmpty()){
            List<ListConsumerTopic> consumerList = consumerRepo.ListConsumerOrderType(orderTypeID, PageRequest.of(page, limit, sort));
            Integer count = consumerRepo.getListConsumerOrderTypeTotal(orderTypeID);
            resp.setCount(count);
            resp.setData(consumerList);
            return resp;
        }else {
            if( !req.getSearchField().isEmpty()){
                if (searchField.equals("system_name")){
                    List<ListConsumerTopic> consumerList = consumerRepo.ListConsumerOrderTypeSystemNameLike(orderTypeID, search, PageRequest.of(page, limit, sort));
                    Integer count = consumerRepo.getListConsumerOrderTypeSystemNameLikeTotal(orderTypeID, search);
                    resp.setCount(count);
                    resp.setData(consumerList);
                    return resp;
                }else if (searchField.equals("contactname")){
                    List<ListConsumerTopic> consumerList = consumerRepo.ListConsumerOrderTypeContactNameLike(orderTypeID, search, PageRequest.of(page, limit, sort));
                    Integer count = consumerRepo.getListConsumerOrderTypeContactNameLikeTotal(orderTypeID, search);
                    resp.setCount(count);
                    resp.setData(consumerList);
                    return resp;
                }else if (searchField.equals("email")){
                    List<ListConsumerTopic> consumerList = consumerRepo.ListConsumerOrderTypeEmailLike(orderTypeID, search, PageRequest.of(page, limit, sort));
                    Integer count = consumerRepo.getListConsumerOrderTypeEmailLikeTotal(orderTypeID, search);
                    resp.setCount(count);
                    resp.setData(consumerList);
                    return resp;
                } else if (searchField.equals("phonenumber")){
                    List<ListConsumerTopic> consumerList = consumerRepo.ListConsumerOrderTypePhoneNumberLike(orderTypeID, search, PageRequest.of(page, limit, sort));
                    Integer count = consumerRepo.getListConsumerOrderTypePhoneNumberLikeTotal(orderTypeID, search);
                    resp.setCount(count);
                    resp.setData(consumerList);
                    return resp;
                }
            }

        
            List<ListConsumerTopic> consumerList = consumerRepo.ListConsumerOrderTypeAllLike(orderTypeID, search, PageRequest.of(page, limit, sort));
            Integer count = consumerRepo.getListConsumerOrderTypeAllLikeTotal(orderTypeID, search);
            resp.setCount(count);
            resp.setData(consumerList);
            return resp;
        }
    }

    @Override
    public PaginationDataResp ListManageConsumers(ListConsumerReq req) {
        PaginationDataResp resp = new PaginationDataResp();
        Integer offset = req.getStart();
        Integer limit = req.getLength();
        Integer page = offset / limit;
        String sortName = req.getSortName();
        String sortBy = req.getSortBy();
        String search = req.getSearch();
        JpaSort sort = JpaSort.unsafe(Sort.Direction.fromString(sortBy), "( con."+sortName+")");
        String searchField = req.getSearchField().toLowerCase();

        if ( search.isEmpty()){
            List<ListConsumerTopic> consumerList = consumerRepo.ListConsumer(PageRequest.of(page, limit, sort));
            Integer count = consumerRepo.getListConsumerTotal();
            resp.setCount(count);
            resp.setData(consumerList);
            return resp;
        }else {
            if( !req.getSearchField().isEmpty()){
                if (searchField.equals("system_name")){
                    List<ListConsumerTopic> consumerList = consumerRepo.ListConsumerSystemNameLike(search, PageRequest.of(page, limit, sort));
                    Integer count = consumerRepo.getListConsumerSystemNameLikeTotal(search);
                    resp.setCount(count);
                    resp.setData(consumerList);
                    return resp;
                }else if (searchField.equals("contactname")){
                    List<ListConsumerTopic> consumerList = consumerRepo.ListConsumerContactNameLike(search, PageRequest.of(page, limit, sort));
                    Integer count = consumerRepo.getListConsumerContactNameLikeTotal(search);
                    resp.setCount(count);
                    resp.setData(consumerList);
                    return resp;
                }else if (searchField.equals("email")){
                    List<ListConsumerTopic> consumerList = consumerRepo.ListConsumerEmailLike(search, PageRequest.of(page, limit, sort));
                    Integer count = consumerRepo.getListConsumerEmailLikeTotal(search);
                    resp.setCount(count);
                    resp.setData(consumerList);
                    return resp;
                } else if (searchField.equals("phonenumber")){
                    List<ListConsumerTopic> consumerList = consumerRepo.ListConsumerPhoneNumberLike(search, PageRequest.of(page, limit, sort));
                    Integer count = consumerRepo.getListConsumerPhoneNumberLikeTotal(search);
                    resp.setCount(count);
                    resp.setData(consumerList);
                    return resp;
                }
            }

        
            List<ListConsumerTopic> consumerList = consumerRepo.ListConsumerAllLike(search, PageRequest.of(page, limit, sort));
            Integer count = consumerRepo.getListConsumerAllLikeTotal(search);
            resp.setCount(count);
            resp.setData(consumerList);
            return resp;
        }
    }

    @Override
    public PaginationDataResp ListManageMetrics(DefaultListReq req) {
        PaginationDataResp resp = new PaginationDataResp();
        Integer offset = req.getStart();
        Integer limit = req.getLength();
        String sortName = req.getSortName();
        String sortBy = req.getSortBy();
        List<SaMetricNotificationEntity> consumerList;
        if (limit == null || limit <= 0) {
            // No limit case, fetch all results
            consumerList = saMetricNotificationRepo.findAll(Sort.by(Sort.Direction.fromString(req.getSortBy()), req.getSortName()));
        } else {
            // Pagination case
            Integer page = offset / limit;
            consumerList = saMetricNotificationRepo.ListSaMetrics(PageRequest.of(page, limit, Sort.Direction.fromString(req.getSortBy()), req.getSortName()));
        }
        Integer count = saMetricNotificationRepo.getTotalCount();
        resp.setCount(count);
        resp.setData(consumerList);
        return resp;
    }

}
