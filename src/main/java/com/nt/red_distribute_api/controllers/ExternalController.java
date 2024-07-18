package com.nt.red_distribute_api.controllers;



import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.red_distribute_api.config.AuthConfig;
import com.nt.red_distribute_api.dto.req.external.ConsumerMessageReq;
import com.nt.red_distribute_api.dto.req.external.SubAndUnsubscribeReq;
import com.nt.red_distribute_api.dto.resp.DefaultListResp;
import com.nt.red_distribute_api.dto.resp.DefaultResp;
import com.nt.red_distribute_api.dto.resp.UserAclsInfo;
import com.nt.red_distribute_api.dto.resp.external.ListConsumeMsg;
import com.nt.red_distribute_api.dto.resp.external.TopicDetailResp;
import com.nt.red_distribute_api.dto.resp.external.VerifyConsumerResp;
import com.nt.red_distribute_api.entity.ConsumerEntity;
import com.nt.red_distribute_api.entity.OrderTypeEntity;
import com.nt.red_distribute_api.entity.UserEntity;
import com.nt.red_distribute_api.entity.view.consumer_ordertype.ConsumerLJoinOrderType;
import com.nt.red_distribute_api.service.ConsumerOrderTypeService;
import com.nt.red_distribute_api.service.ConsumerService;
import com.nt.red_distribute_api.service.GafranaService;
import com.nt.red_distribute_api.service.KafkaClientService;
import com.nt.red_distribute_api.service.OrderTypeService;
import com.nt.red_distribute_api.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    private UserService userService;
    
    @Autowired
    private GafranaService gafranaService;

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
                // verifyData.setRemark("password: " + password+", passwordEncode: " + passwordEncode+ "isverify password:"+verifyPassword(password, passwordEncode));
                if (verifyPassword(password, passwordEncode)){
                    verifyData.setRealPassword(password);
                    verifyData.setIsVerify(true);
                }
            }
        }
        return verifyData;
    }

    protected VerifyConsumerResp VerifyDistributeUserAuth(String authHeader){
        VerifyConsumerResp verifyData = new VerifyConsumerResp();
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            // Extract and decode the base64 encoded credentials
            String base64Credentials = authHeader.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
            
            // Split the username and password tokens
            final String[] values = credentials.split(":", 2);
            String username = values[0];
            String password = values[1];
            UserEntity user = userService.findUserLogin(username);
            if (user!= null){
                String passwordEncode = user.getPassword();
                // verifyData.setRemark("password: " + password+", passwordEncode: " + passwordEncode+ "isverify password:"+verifyPassword(password, passwordEncode));
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
        HttpServletRequest request
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

            String orderTypeTopicNames = "";
            try{
                List<ConsumerLJoinOrderType> orderCons = consumerOrderTypeService.ListConsumerOrderType(vsp.getConsumerData().getID());
                for (ConsumerLJoinOrderType orderTypeData : orderCons){
                    orderTypeTopicNames+=orderTypeData.getORDERTYPE_NAME().toUpperCase()+",";
                }
                
            } catch (Exception e){
                resp.setError(e.getLocalizedMessage());
                resp.setMessage("Error while list order type for subscribe: " + e.getMessage());
                return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if(orderTypeTopicNames.length()<=0){
                resp.setMessage("Consumer have not subscribe any topic!");
                return new ResponseEntity<>( resp, HttpStatus.OK);
            }


            TopicDetailResp data = kafkaClientService.getTopicDescriptionByConsumer(
                vsp.getConsumerData().getUsername(), 
                vsp.getRealPassword(),
                vsp.getConsumerData().getConsumer_group().toUpperCase(), 
                orderTypeTopicNames
            );
            if (data.getError() != null){
                resp.setError(data.getError());
                resp.setMessage("Error while topic_detail : " + data.getError());
                return new ResponseEntity<>( resp, HttpStatus.BAD_REQUEST);
            }
            try{
                // HashMap<String, Object> rep = new HashMap<String, Object>();
                // List<Object> topicOffset = kafkaClientService.getTopicOffset();
                // rep.put("topicOffset", topicOffset);
                // rep.put("data", data.getData());
                // resp.setResult(rep);
                resp.setResult(data.getData());
                resp.setMessage("Success!");
                // resp.setMessage(orderTypeTopicNames);
                return new ResponseEntity<>( resp, HttpStatus.OK);
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
        @RequestPart("topic") String topic,
        @RequestPart("messages") MultipartFile file
        
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

            // Process file
            ObjectMapper mapper = new ObjectMapper();
            try {
                // Read file content and parse it as a JSON array
                String content = new String(file.getBytes());
                JsonNode jsonArray = mapper.readTree(content);

                // Loop through the JSON array and print each JSON object
                if (jsonArray.isArray()) {
                    String errorMsgs = null;
                    for (JsonNode jsonMessage : jsonArray) {
                        System.out.println(jsonMessage.toString());
                        String err = kafkaClientService.consumerPublishMessage( 
                            vsp.getConsumerData().getUsername(),
                            vsp.getRealPassword(),
                            topic, 
                            jsonMessage.toString()
                        );
                        if (err !=null) {
                            errorMsgs = errorMsgs+"\n"+err;
                            continue;
                        }
                    }

                    if (errorMsgs != null) {
                        resp.setError(errorMsgs);
                        resp.setMessage("error occurred");
                        return new ResponseEntity<>( resp, HttpStatus.BAD_REQUEST);
                    }
                    resp.setResult(jsonArray);
                    resp.setMessage("Successfully published");
                    return new ResponseEntity<>( resp, HttpStatus.OK);
                }else{
                    resp.setResult(jsonArray);
                    resp.setMessage("No data available");
                    return new ResponseEntity<>( resp, HttpStatus.BAD_REQUEST);
                }
                

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed Error while publish :"+e.getMessage());
            }
            
        }catch (Exception e){
            resp.setError(e.getLocalizedMessage());
            resp.setMessage("Error while publish : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/consume")
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
                    req.getTopicName(), 
                    vsp.getConsumerData().getConsumer_group().toUpperCase(),
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
                resp.setCount(consumeMsgs.getCount());
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

    @PostMapping("/subscribe")
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
                    List<ConsumerLJoinOrderType> orderCons = consumerOrderTypeService.ListConsumerOrderType(vsp.getConsumerData().getID());
                    for (ConsumerLJoinOrderType orderTypeData : orderCons){
                        orderTypeIDs.add(orderTypeData.getID());
                        orderTypeTopicNames.add(orderTypeData.getORDERTYPE_NAME().toUpperCase());
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

    @PostMapping("/unsubscribe")
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
                    List<ConsumerLJoinOrderType> orderCons = consumerOrderTypeService.ListConsumerOrderType(vsp.getConsumerData().getID());
                    for (ConsumerLJoinOrderType orderTypeData : orderCons){
                        orderTypeIDs.add(orderTypeData.getID());
                        orderTypeTopicNames.add(orderTypeData.getORDERTYPE_NAME().toUpperCase());
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

    @PostMapping("/export_alarm")
    public ResponseEntity<Object> exportAlarm(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestParam(name="type", defaultValue ="csv") String exportType
    ) {
        DefaultListResp resp = new DefaultListResp();
        try{
            String requestHeader = request.getHeader("Authorization");
            VerifyConsumerResp vsp = VerifyDistributeUserAuth(requestHeader);
            if (!vsp.getIsVerify()){
                resp.setError("Authenticated not you.");
                resp.setMessage("You don't have permission!!!");
                return new ResponseEntity<>( resp, HttpStatus.UNAUTHORIZED);
            }
            
            
            try {
                gafranaService.ExportAlertAlarm(response, exportType);
    
                return ResponseEntity.ok(null);
            } catch (IOException e) {
                resp.setMessage("Error while get alert history: " + e.getMessage());
                return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }catch (Exception e){
            resp.setError(e.getLocalizedMessage());
            resp.setMessage("Error while export alert alarm : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
