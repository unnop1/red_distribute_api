package com.nt.red_distribute_api.controllers;



import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nt.red_distribute_api.config.AuthConfig;
import com.nt.red_distribute_api.dto.req.external.PublishMessageReq;
import com.nt.red_distribute_api.dto.resp.DefaultControllerResp;
import com.nt.red_distribute_api.dto.resp.DefaultListResp;
import com.nt.red_distribute_api.dto.resp.DefaultResp;
import com.nt.red_distribute_api.dto.resp.UserAclsInfo;
import com.nt.red_distribute_api.dto.resp.external.VerifyConsumerResp;
import com.nt.red_distribute_api.entity.ConsumerEntity;
import com.nt.red_distribute_api.entity.OrderTypeEntity;
import com.nt.red_distribute_api.entity.UserEntity;
import com.nt.red_distribute_api.entity.view.consumer_ordertype.ConsumerLJoinOrderType;
import com.nt.red_distribute_api.service.ConsumerOrderTypeService;
import com.nt.red_distribute_api.service.ConsumerService;
import com.nt.red_distribute_api.service.KafkaClientService;
import com.nt.red_distribute_api.service.OrderTypeService;
import com.nt.red_distribute_api.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/external")
public class ExternalController {

    @Autowired
    private AuthConfig authConfig;

    @Autowired
    private ConsumerOrderTypeService consumerOrderTypeService;

    @Autowired
    private OrderTypeService orderTypService;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private KafkaClientService kafkaClientService;

    protected VerifyConsumerResp VerifyAuthentication(String authHeader){
        VerifyConsumerResp verifyData = new VerifyConsumerResp();
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            // Extract and decode the base64 encoded credentials
            String base64Credentials = authHeader.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
            
            // Split the username and password tokens
            final String[] values = credentials.split(":", 2);
            String username = values[0];
            String password = values[1];
            String passwordEncode = authConfig.passwordEncoder().encode(password);

            ConsumerEntity consumer = consumerService.getConsumerByUsername(username);
            if (consumer.getPassword().equals(passwordEncode)){
                verifyData.setConsumerData(consumer);
                verifyData.setIsVerify(true);
            }
        }
        return verifyData;
    }

    @GetMapping("/topics")
    public ResponseEntity<Object> QueueDetail(
        HttpServletRequest request,
        @RequestParam(name = "topic_name")String topic_name
    ) {
        DefaultResp resp = new DefaultResp();
        try{
            String requestHeader = request.getHeader("Authorization");
            VerifyConsumerResp vsp = VerifyAuthentication(requestHeader);
            if (!vsp.getIsVerify()){
                resp.setError("Authenticated not you.");
                resp.setMessage("You don't have permission!!!");
                return new ResponseEntity<>( resp, HttpStatus.UNAUTHORIZED);
            }

            OrderTypeEntity data = orderTypService.getOrderTypeByName(topic_name);
            resp.setResult(data);
            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setError(e.getLocalizedMessage());
            resp.setMessage("Error while topic_detail : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/publish")
    public ResponseEntity<Object> publishMessageToTopic(
        HttpServletRequest request,
        @RequestBody PublishMessageReq data
    ) {
        DefaultResp resp = new DefaultResp();
        try{
            String requestHeader = request.getHeader("Authorization");
            VerifyConsumerResp vsp = VerifyAuthentication(requestHeader);
            if (!vsp.getIsVerify()){
                resp.setError("Authenticated not you.");
                resp.setMessage("You don't have permission!!!");
                return new ResponseEntity<>( resp, HttpStatus.UNAUTHORIZED);
            }
            kafkaClientService.consumerPublishMessage( vsp.getConsumerData().getUsername(),vsp.getConsumerData().getPassword() ,data.getTopic(), data.getMessage());
            resp.setResult(data);
            resp.setMessage("Successfully published");
            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setError(e.getLocalizedMessage());
            resp.setMessage("Error while publish : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("consume")
    public ResponseEntity<Object> ConsumeAllMessagesInTopic(
        HttpServletRequest request
    ) {
        DefaultListResp resp = new DefaultListResp();
        try{
            String requestHeader = request.getHeader("Authorization");
            VerifyConsumerResp vsp = VerifyAuthentication(requestHeader);
            if (!vsp.getIsVerify()){
                resp.setError("Authenticated not you.");
                resp.setMessage("You don't have permission!!!");
                return new ResponseEntity<>( resp, HttpStatus.UNAUTHORIZED);
            }

            List<String> messages = kafkaClientService.consumeMessages(
                vsp.getConsumerData().getUsername(),
                vsp.getConsumerData().getPassword(),
                requestHeader, requestHeader
            );

            resp.setResult(messages);
            resp.setCount(messages.size());
            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setError(e.getLocalizedMessage());
            resp.setMessage("Error while consume : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("subscribe_unsubscribe")
    public ResponseEntity<Object> subscribeUnsubscribeTopic(
        HttpServletRequest request
    ) {
        DefaultListResp resp = new DefaultListResp();
        try{
            String requestHeader = request.getHeader("Authorization");
            VerifyConsumerResp vsp = VerifyAuthentication(requestHeader);
            if (!vsp.getIsVerify()){
                resp.setError("Authenticated not you.");
                resp.setMessage("You don't have permission!!!");
                return new ResponseEntity<>( resp, HttpStatus.UNAUTHORIZED);
            }

            List<ConsumerLJoinOrderType> consumerOrderTypes = consumerOrderTypeService.ListConsumerOrderType(vsp.getConsumerData().getID());
            List<String> orderTypeTopicNames = new ArrayList<>();
                for (ConsumerLJoinOrderType consumerOrderType : consumerOrderTypes){
                    orderTypeTopicNames.add(consumerOrderType.getORDERTYPE_NAME());
                }
                List<UserAclsInfo> userAclsTopics = kafkaClientService.initUserAclsTopicList(vsp.getConsumerData().getUsername(), orderTypeTopicNames);
            if(consumerOrderTypes.size()>0){
                kafkaClientService.deleteAcls(vsp.getConsumerData().getUsername(), userAclsTopics);
            }else{
                kafkaClientService.createAcls(vsp.getConsumerData().getUsername(), userAclsTopics, vsp.getConsumerData().getConsumer_group());
            }
        


            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setError(e.getLocalizedMessage());
            resp.setMessage("Error while subscribe_unsubscribe : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
