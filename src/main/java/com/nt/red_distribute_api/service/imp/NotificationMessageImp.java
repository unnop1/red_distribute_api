package com.nt.red_distribute_api.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.dto.req.DefaultListReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.entity.NotificationMsgEntity;
import com.nt.red_distribute_api.entity.view.consumer.ListConsumerTopic;
import com.nt.red_distribute_api.repo.NotificationMessageRepo;
import com.nt.red_distribute_api.service.NotificationMessageService;

@Service
public class NotificationMessageImp implements NotificationMessageService{

    @Autowired
    private NotificationMessageRepo notificationMessageRepo;

    @Override
    public PaginationDataResp ListMetricNotificationMessages(DefaultListReq req) {
        PaginationDataResp resp = new PaginationDataResp();
        Integer offset = req.getStart();
        Integer limit = req.getLength();
        Integer page = offset / limit;
        String sortName = req.getSortName();
        String sortBy = req.getSortBy();
        String search = req.getSearch();
        String searchField = req.getSearchField().toLowerCase();

        
        List<NotificationMsgEntity> consumerList = notificationMessageRepo.ListNotificationMsg(PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName));
        Integer count = notificationMessageRepo.getTotalCount();
        resp.setCount(count);
        resp.setData(consumerList);
        return resp;
    }

}
