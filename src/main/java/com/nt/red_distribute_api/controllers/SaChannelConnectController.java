package com.nt.red_distribute_api.controllers;

import org.springframework.web.bind.annotation.*;

import com.nt.red_distribute_api.dto.req.audit.ListAuditReq;
import com.nt.red_distribute_api.dto.resp.DefaultControllerResp;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.service.AuditService;
import com.nt.red_distribute_api.service.SaChannelConnectService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/sa_channel_connect")
public class SaChannelConnectController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private SaChannelConnectService saChannelConnectService;


    @GetMapping("list")
    public ResponseEntity<DefaultControllerResp> ListSaChannelConnect() {  
        DefaultControllerResp response = new DefaultControllerResp();
        PaginationDataResp listChannels = saChannelConnectService.ListChannelConnects();
        response.setRecordsFiltered(listChannels.getCount());
        response.setRecordsTotal(listChannels.getCount());
        response.setCount(listChannels.getCount());
        response.setMessage("Success");
        response.setData(listChannels.getData());
        response.setStatusCode(200);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    
}