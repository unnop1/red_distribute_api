package com.nt.red_distribute_api.controllers;



import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.red_distribute_api.config.AuthConfig;
import com.nt.red_distribute_api.dto.req.external.ConsumerMessageReq;
import com.nt.red_distribute_api.dto.req.external.PublishMessageReq;
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
                    consumer.setPassword(password);
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
                resp.setMessage("You don't have permission!!! username : " + vsp.getConsumerData().getUsername());
                return new ResponseEntity<>( resp, HttpStatus.UNAUTHORIZED);
            }

            // List<OrderTypeEntity> data = orderTypService.ListAll();
            TopicDetailResp data = kafkaClientService.getTopicDescription(topicName);
            if (data.getError() != null){
                resp.setError(data.getError());
                resp.setMessage("Error while topic_detail : " + data.getError());
                return new ResponseEntity<>( resp, HttpStatus.BAD_REQUEST);
            }
            resp.setResult(data);
            resp.setMessage(vsp.getRemark());
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

            ObjectMapper mapper = new ObjectMapper();

            String objectString = mapper.writeValueAsString(data.getMessage());

            String err = kafkaClientService.consumerPublishMessage( 
                vsp.getConsumerData().getUsername(),
                vsp.getConsumerData().getPassword(),
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

            ListConsumeMsg consumeMsgs = kafkaClientService.consumeMessages(
                vsp.getConsumerData().getUsername(),
                vsp.getConsumerData().getPassword(),
                req.getTopicName(), vsp.getConsumerData().getConsumer_group(),
                1000
            );
            if (consumeMsgs.getErr() != null){
                resp.setError(consumeMsgs.getErr());
                resp.setMessage("Error while consuming messages");
                return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
            if(consumeMsgs.getMessages() != null){
                resp.setResult(consumeMsgs.getMessages());
                resp.setCount(consumeMsgs.getMessages().size());
                return new ResponseEntity<>( resp, HttpStatus.OK);
            }
            
            // try{
            //     ObjectMapper objectMapper = new ObjectMapper();
            //     // resp.setMessage(objectMapper.writeValueAsString(consumeMsgs));
            // }catch (Exception e){
            //     resp.setError(e.getMessage());
            // }
            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setError(e.getLocalizedMessage());
            resp.setMessage("Error while consume : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("subscribe")
    public ResponseEntity<Object> subscribeTopic(
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
            
            List<OrderTypeEntity> orderTypeLists = orderTypService.ListAll();
            List<ConsumerLJoinOrderType> consumerOrderTypes = consumerOrderTypeService.ListConsumerOrderType(vsp.getConsumerData().getID());
            List<String> orderTypeTopicNames = new ArrayList<>();
            List<Long> orderTypeIDs = new ArrayList<>();
            for (ConsumerLJoinOrderType consumerOrderType : consumerOrderTypes){
                orderTypeTopicNames.add(consumerOrderType.getORDERTYPE_NAME());
            }
            for (OrderTypeEntity orderTypeData : orderTypeLists){
                orderTypeIDs.add(orderTypeData.getID());
            }

            Error err = consumerOrderTypeService.updateConsumerOrderType(vsp.getConsumerData().getID(), orderTypeIDs, vsp.getConsumerData().getUsername());
            if (err != null){
                resp.setError(err.getLocalizedMessage());
                resp.setMessage(err.getMessage());
                return new ResponseEntity<>( resp, HttpStatus.BAD_REQUEST);
            }

            List<UserAclsInfo> userAclsTopics = kafkaClientService.initUserAclsTopicList(vsp.getConsumerData().getUsername(), orderTypeTopicNames);
            
            kafkaClientService.createAcls(vsp.getConsumerData().getUsername(), userAclsTopics, vsp.getConsumerData().getConsumer_group());
            resp.setResult("subscribed consumer "+vsp.getConsumerData().getID()+", username:"+vsp.getConsumerData().getUsername());
            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setError(e.getLocalizedMessage());
            resp.setMessage("Error while subscribe_unsubscribe : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("unsubscribe")
    public ResponseEntity<Object> unsubscribeTopic(
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
            List<Long> clearOrderTypes = new ArrayList<Long>();
            Error err = consumerOrderTypeService.updateConsumerOrderType(vsp.getConsumerData().getID(), clearOrderTypes, vsp.getConsumerData().getUsername());
            if(err!=null){
                resp.setCount(0);
                resp.setError(err.getLocalizedMessage());
                resp.setMessage(err.getMessage());
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
            }

            List<ConsumerLJoinOrderType> consumerOrderTypes = consumerOrderTypeService.ListConsumerOrderType(vsp.getConsumerData().getID());
            List<String> orderTypeTopicNames = new ArrayList<>();
            for (ConsumerLJoinOrderType consumerOrderType : consumerOrderTypes){
                orderTypeTopicNames.add(consumerOrderType.getORDERTYPE_NAME());
            }
            List<UserAclsInfo> userAclsTopics = kafkaClientService.initUserAclsTopicList(vsp.getConsumerData().getUsername(), orderTypeTopicNames);

            kafkaClientService.deleteAcls(vsp.getConsumerData().getUsername(), userAclsTopics);

            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setError(e.getLocalizedMessage());
            resp.setMessage("Error while subscribe_unsubscribe : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
