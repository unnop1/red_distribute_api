package com.nt.red_distribute_api.controllers;



import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.red_distribute_api.client.KafkaListTopicsResp;
import com.nt.red_distribute_api.config.AuthConfig;
import com.nt.red_distribute_api.dto.req.external.ConsumerMessageReq;
import com.nt.red_distribute_api.dto.req.external.PublishMessageReq;
import com.nt.red_distribute_api.dto.req.external.SubAndUnsubscribeReq;
import com.nt.red_distribute_api.dto.resp.DefaultListResp;
import com.nt.red_distribute_api.dto.resp.DefaultResp;
import com.nt.red_distribute_api.dto.resp.UserAclsInfo;
import com.nt.red_distribute_api.dto.resp.external.ListConsumeMsg;
import com.nt.red_distribute_api.dto.resp.external.TopicDetailResp;
import com.nt.red_distribute_api.dto.resp.external.VerifyConsumerResp;
import com.nt.red_distribute_api.entity.ConsumerEntity;
import com.nt.red_distribute_api.entity.OrderTypeEntity;
import com.nt.red_distribute_api.entity.view.consumer_ordertype.ConsumerLJoinOrderType;
import com.nt.red_distribute_api.service.ConsumerOrderTypeService;
import com.nt.red_distribute_api.service.ConsumerService;
import com.nt.red_distribute_api.service.KafkaClientService;
import com.nt.red_distribute_api.service.KafkaProducerService;
import com.nt.red_distribute_api.service.OrderTypeService;
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
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private KafkaClientService kafkaClientService;

    public boolean verifyPassword(String rawPassword, String storedEncodedPassword) {
        return authConfig.passwordEncoder().matches(rawPassword, storedEncodedPassword);
    }

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
            ConsumerEntity consumer = consumerService.getConsumerByUsername(username);
            if (consumer!= null){
                String passwordEncode = consumer.getPassword();
                verifyData.setConsumerData(consumer);
                verifyData.setRemark("password: " + password+", passwordEncode: " + passwordEncode+ "isverify password:"+verifyPassword(password, passwordEncode));
                if (verifyPassword(password, passwordEncode)){
                    verifyData.setRealPassword(password);
                    verifyData.setIsVerify(true);
                }
            }
        }
        return verifyData;
    }

    @GetMapping("/topics")
    public ResponseEntity<Object> QueueDetail(
        HttpServletRequest request,
        @RequestParam(name="topic_name", defaultValue = "all") String topicName
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

            // // List<OrderTypeEntity> data = orderTypService.ListAll();
            // TopicDetailResp data = kafkaClientService.getTopicDescription(topicName);
            // if (data.getError() != null){
            //     resp.setError(data.getError());
            //     resp.setMessage("Error while topic_detail : " + data.getError());
            //     return new ResponseEntity<>( resp, HttpStatus.BAD_REQUEST);
            // }
            try{
                KafkaListTopicsResp kafkaTopicResp = kafkaClientService.getKafkaTopicList(topicName);

                if(kafkaTopicResp!= null){
                    if(kafkaTopicResp.getError()!= null){
                        resp.setResult(kafkaTopicResp.getError());
                        resp.setMessage("Error!");
                        return new ResponseEntity<>( resp, HttpStatus.BAD_REQUEST);
                    }

                    if(topicName.equals("all")){
                        resp.setResult(kafkaTopicResp.getTopics().getTopics());
                        resp.setMessage("Success!");
                        return new ResponseEntity<>( resp, HttpStatus.OK);
                    }else{
                        JSONArray listTopics = kafkaTopicResp.getTopics().getTopics();
                        for(int i=0;i<listTopics.length();i++){
                            JSONObject foundTopic = listTopics.getJSONObject(i);
                            if(foundTopic.has("name")){
                                String foundTopicName = foundTopic.getString("name");
                                if(foundTopicName.equals(topicName)){
                                    resp.setResult(foundTopic);
                                    resp.setMessage("Success!");
                                    return new ResponseEntity<>( resp, HttpStatus.OK);
                                }
                            }
                        }
                    }
                }

                resp.setResult("Not Found Topics");
                resp.setMessage("NOT FOUND!");
                return new ResponseEntity<>( resp, HttpStatus.NOT_FOUND);
            }catch (Exception e){
                resp.setError(e.getLocalizedMessage());
                resp.setMessage("Error while resp topic_detail : " + e.getMessage());
                return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }
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

            ObjectMapper mapper = new ObjectMapper();

            String objectString = mapper.writeValueAsString(data.getMessage());

            String err = kafkaClientService.consumerPublishMessage( 
                vsp.getConsumerData().getUsername(),
                vsp.getRealPassword(),
                data.getTopic(), 
                objectString
            );
            if (err !=null) {
                resp.setMessage(err);
                return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }
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
        HttpServletRequest request,
        @RequestBody ConsumerMessageReq req
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

            ListConsumeMsg consumeMsgs= new ListConsumeMsg();
            try{

                consumeMsgs = kafkaClientService.consumeMessages(
                    vsp.getConsumerData().getUsername(),
                    vsp.getRealPassword(),
                    req.getTopicName(), vsp.getConsumerData().getConsumer_group().toUpperCase(),
                    req.getOffset(),
                    req.getLimit()
                );
                try{
                    if (consumeMsgs.getErr() != null){
                        resp.setError(consumeMsgs.getErr());
                        resp.setMessage("Error while consuming messages");
                        return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
                    } 
                }catch (Exception e){
                    consumeMsgs.setErr(e.getMessage());
                    resp.setMessage("Error while response in case1:"+ e.getMessage());
                    return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }catch (Exception e){
                consumeMsgs.setErr(e.getMessage());
                resp.setMessage("Error while consuming case1:"+ e.getMessage());
                return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            try{
                if(consumeMsgs.getErr() != null){
                    // if(consumeMsgs.getMessages() != null){
                        resp.setError(consumeMsgs.getErr());
                        // resp.setCount(consumeMsgs.getMessages().size());
                        return new ResponseEntity<>( resp, HttpStatus.BAD_GATEWAY);
                }
                resp.setCount(consumeMsgs.getMessages().size());
                resp.setResult(consumeMsgs.getMessages());
            }catch (Exception e){
                resp.setMessage("Error while consuming case2:"+ e.getMessage());
                return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setError(e.getLocalizedMessage());
            resp.setMessage("Error while consume : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("subscribe")
    public ResponseEntity<Object> subscribeTopic(
        HttpServletRequest request,
        @RequestBody SubAndUnsubscribeReq req
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
            
            List<String> orderTypeTopicNames = new ArrayList<>();
            List<Long> orderTypeIDs = new ArrayList<>();
            try{
                if(req.getTopicName().toLowerCase().equals("all")){
                    List<OrderTypeEntity> orderTypeLists = orderTypService.ListAll();
                    for (OrderTypeEntity orderTypeData : orderTypeLists){
                        orderTypeIDs.add(orderTypeData.getID());
                        orderTypeTopicNames.add(orderTypeData.getOrderTypeName().toUpperCase());
                    }
                }else{
                    OrderTypeEntity orderTypeDetail = orderTypService.getOrderTypeByName(req.getTopicName());
                    if(orderTypeDetail != null){
                        orderTypeIDs.add(orderTypeDetail.getID());
                        orderTypeTopicNames.add(orderTypeDetail.getOrderTypeName().toUpperCase());
                    }
                }
                
            } catch (Exception e){
                resp.setError(e.getLocalizedMessage());
                resp.setMessage("Error while list order type for subscribe: " + e.getMessage());
                return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
            try{
                Error err = consumerOrderTypeService.updateConsumerOrderType(vsp.getConsumerData().getID(), orderTypeIDs, vsp.getConsumerData().getUsername());
                if (err != null){
                    resp.setError(err.getLocalizedMessage());
                    resp.setMessage(err.getMessage());
                    return new ResponseEntity<>( resp, HttpStatus.BAD_REQUEST);
                }

            }catch (Exception e){
                resp.setError(e.getLocalizedMessage());
                resp.setMessage("Error while updateConsumerOrderType: " + e.getMessage());
                return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            try{
                List<UserAclsInfo> userAclsTopics = kafkaClientService.initUserAclsTopicList(vsp.getConsumerData().getUsername(), orderTypeTopicNames);
                
                try{
                    kafkaClientService.createAcls(vsp.getConsumerData().getUsername(), userAclsTopics, vsp.getConsumerData().getConsumer_group());
                    resp.setResult(userAclsTopics);
                    resp.setCount(orderTypeIDs.size());
                }catch (Exception e){
                    resp.setResult(orderTypeTopicNames);
                    resp.setError(e.getLocalizedMessage());
                    resp.setMessage("Error while createAcls: " + e.getMessage());
                    return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }catch (Exception e){
                resp.setResult(orderTypeTopicNames);
                resp.setError(e.getLocalizedMessage());
                resp.setMessage("Error while initUserAclsTopicList for subscribe: " + e.getMessage());
                return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setError(e.getLocalizedMessage());
            resp.setMessage("Error while subscribe : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("unsubscribe")
    public ResponseEntity<Object> unsubscribeTopic(
        HttpServletRequest request,
        @RequestBody SubAndUnsubscribeReq req
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
            
            List<String> orderTypeTopicNames = new ArrayList<>();
            List<Long> orderTypeIDs = new ArrayList<>();
            try{
                if(req.getTopicName().toLowerCase().equals("all")){
                    List<OrderTypeEntity> orderTypeLists = orderTypService.ListAll();
                    for (OrderTypeEntity orderTypeData : orderTypeLists){
                        orderTypeIDs.add(orderTypeData.getID());
                        orderTypeTopicNames.add(orderTypeData.getOrderTypeName().toUpperCase());
                    }
                }else{
                    OrderTypeEntity orderTypeDetail = orderTypService.getOrderTypeByName(req.getTopicName());
                    if(orderTypeDetail != null){
                        orderTypeIDs.add(orderTypeDetail.getID());
                        orderTypeTopicNames.add(orderTypeDetail.getOrderTypeName().toUpperCase());
                    }
                }
                
            } catch (Exception e){
                resp.setError(e.getLocalizedMessage());
                resp.setMessage("Error while list order type for unsubscribe: " + e.getMessage());
                return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
            try{
                Error err = consumerOrderTypeService.updateConsumerOrderType(vsp.getConsumerData().getID(), orderTypeIDs, vsp.getConsumerData().getUsername());
                if (err != null){
                    resp.setError(err.getLocalizedMessage());
                    resp.setMessage(err.getMessage());
                    return new ResponseEntity<>( resp, HttpStatus.BAD_REQUEST);
                }

            }catch (Exception e){
                resp.setError(e.getLocalizedMessage());
                resp.setMessage("Error while updateConsumerOrderType: " + e.getMessage());
                return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            try{
                List<UserAclsInfo> userAclsTopics = kafkaClientService.initUserAclsTopicList(vsp.getConsumerData().getUsername(), orderTypeTopicNames);
                
                try{
                    kafkaClientService.deleteAcls(vsp.getConsumerData().getUsername(), userAclsTopics);
                    resp.setResult(userAclsTopics);
                    resp.setCount(orderTypeIDs.size());
                }catch (Exception e){
                    resp.setResult(orderTypeTopicNames);
                    resp.setError(e.getLocalizedMessage());
                    resp.setMessage("Error while deleteAcls: " + e.getMessage());
                    return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }catch (Exception e){
                resp.setResult(orderTypeTopicNames);
                resp.setError(e.getLocalizedMessage());
                resp.setMessage("Error while initUserAclsTopicList for unsubscribe: " + e.getMessage());
                return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setError(e.getLocalizedMessage());
            resp.setMessage("Error while unsubscribe : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
