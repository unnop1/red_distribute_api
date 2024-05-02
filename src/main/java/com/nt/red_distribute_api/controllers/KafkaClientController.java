package com.nt.red_distribute_api.controllers;

import org.springframework.web.bind.annotation.*;

import com.nt.red_distribute_api.dto.req.audit.ListAuditReq;
import com.nt.red_distribute_api.dto.resp.DefaultControllerResp;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.service.AuditService;
import com.nt.red_distribute_api.service.KafkaClientService;

import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/kafka_client")
public class KafkaClientController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private KafkaClientService kafkaClientService;


    @DeleteMapping("purge")
    public ResponseEntity<DefaultControllerResp> PurgeMessageTopic(
        @RequestBody String topicName
    ) {   
        DefaultControllerResp response = new DefaultControllerResp();
        
        response.setMessage("Success");
        response.setStatusCode(200);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    
}