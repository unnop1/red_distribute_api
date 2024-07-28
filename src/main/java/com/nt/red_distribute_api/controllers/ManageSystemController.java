package com.nt.red_distribute_api.controllers;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.red_distribute_api.Auth.JwtHelper;
import com.nt.red_distribute_api.Util.CustomServlet;
import com.nt.red_distribute_api.Util.DateTime;
import com.nt.red_distribute_api.dto.req.DefaultListReq;
import com.nt.red_distribute_api.dto.req.audit.AuditLog;
import com.nt.red_distribute_api.dto.req.consumer.AddConsumerReq;
import com.nt.red_distribute_api.dto.req.consumer.ListConsumerReq;
import com.nt.red_distribute_api.dto.req.consumer.UpdateByConsumerReq;
import com.nt.red_distribute_api.dto.req.external.PublishMessageReq;
import com.nt.red_distribute_api.dto.req.kafka.TopicReq;
import com.nt.red_distribute_api.dto.req.manage_system.ListConsumerByOrderTypeReq;
import com.nt.red_distribute_api.dto.req.ordertype.AddOrderTypeReq;
import com.nt.red_distribute_api.dto.req.ordertype.OrderTypeMoreDetailReq;
import com.nt.red_distribute_api.dto.req.ordertype.UpdateOrderTypeReq;
import com.nt.red_distribute_api.dto.req.sa_metric_notification.AddMetricNotificationReq;
import com.nt.red_distribute_api.dto.req.sa_metric_notification.UpdateMetricReq;
import com.nt.red_distribute_api.dto.resp.ConsumerODTDetailResp;
import com.nt.red_distribute_api.dto.resp.DefaultControllerResp;
import com.nt.red_distribute_api.dto.resp.DefaultResp;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.dto.resp.UserAclsInfo;
import com.nt.red_distribute_api.dto.resp.VerifyAuthResp;
import com.nt.red_distribute_api.dto.resp.external.ConsumeMessage;
import com.nt.red_distribute_api.dto.resp.external.ListConsumeMsg;
import com.nt.red_distribute_api.dto.resp.external.TopicDetailResp;
import com.nt.red_distribute_api.entity.ConsumerEntity;
import com.nt.red_distribute_api.entity.OrderTypeEntity;
import com.nt.red_distribute_api.entity.SaMetricNotificationEntity;
import com.nt.red_distribute_api.entity.view.consumer_ordertype.ConsumerLJoinOrderType;
import com.nt.red_distribute_api.log.LogFlie;
import com.nt.red_distribute_api.service.AuditService;
import com.nt.red_distribute_api.service.ConsumerOrderTypeService;
import com.nt.red_distribute_api.service.ConsumerService;
import com.nt.red_distribute_api.service.KafkaClientService;
import com.nt.red_distribute_api.service.KafkaUIService;
import com.nt.red_distribute_api.service.ManageSystemService;
import com.nt.red_distribute_api.service.NotificationMessageService;
import com.nt.red_distribute_api.service.OrderTypeService;
import com.nt.red_distribute_api.service.SaMetricNotificationService;

import jakarta.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.kafka.common.protocol.types.Field.Bool;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manage_system")
public class ManageSystemController {

    @Autowired
    private JwtHelper helper;

    @Autowired
    private AuditService auditService;
    
    @Autowired
    private KafkaClientService kafkaClientService;

    @Autowired
    private KafkaUIService kafkaUIService;

    @Autowired
    private ManageSystemService manageSystemService;

    @Autowired
    private NotificationMessageService notificationMsgService;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private OrderTypeService orderTypeService;

    @Autowired
    private ConsumerOrderTypeService consumerOrderTypeService;

    @Autowired
    private SaMetricNotificationService saMetricNotificationService;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @GetMapping("/order_types")
    public ResponseEntity<DefaultControllerResp> getManageOrderTypes(
        HttpServletRequest request,    
        @RequestParam(name = "page", defaultValue = "1")Integer page,
        @RequestParam(name = "limit", defaultValue = "10")Integer limit
    ){
        
        DefaultControllerResp resp = new DefaultControllerResp();
        String ipAddress = CustomServlet.getClientIpAddress(request);
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        try {
            PaginationDataResp manageOrderTypes = manageSystemService.ListManageOrderTypes(page, limit);
            
            resp.setCount(manageOrderTypes.getCount());
            resp.setData(manageOrderTypes.getData());
            resp.setStatusCode(HttpStatus.OK.value());
            resp.setMessage("Successfully");

            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while updating : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/order_type/consumers")
    public ResponseEntity<DefaultControllerResp> getAllConsumerByOrderType(
        HttpServletRequest request,    
        @RequestParam(name = "draw", defaultValue = "11")Integer draw,
        @RequestParam(name = "order[0][dir]", defaultValue = "ASC")String sortBy,
        @RequestParam(name = "order[0][name]", defaultValue = "created_date")String sortName,
        @RequestParam(name = "start_time", defaultValue = "")String startTime,
        @RequestParam(name = "end_time", defaultValue = "")String endTime,
        @RequestParam(name = "start", defaultValue = "0")Integer start,
        @RequestParam(name = "length", defaultValue = "10")Integer length,
        @RequestParam(name = "Search", defaultValue = "")String search,
        @RequestParam(name = "Search_field", defaultValue = "")String searchField,
        @RequestParam(name = "order_type_id")Long orderTypeID
    ){
        
        DefaultControllerResp resp = new DefaultControllerResp();
        String ipAddress = CustomServlet.getClientIpAddress(request);
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        try {
            ListConsumerByOrderTypeReq req = new ListConsumerByOrderTypeReq(
                draw,
                sortBy,
                sortName,
                start,
                length,
                search,
                searchField,
                orderTypeID)
            ;

            PaginationDataResp data = manageSystemService.ListConsumerByOrderType(req);
            resp.setData(data.getData());
            resp.setCount(data.getCount());
            resp.setRecordsFiltered(data.getCount());
            resp.setRecordsTotal(data.getCount());
            resp.setDraw(draw);
            
            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setDraw(draw);
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while updating : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @PostMapping("/order_type")
    public ResponseEntity<DefaultControllerResp> createOrderType(HttpServletRequest request, @RequestBody AddOrderTypeReq req) {
        String requestHeader = request.getHeader("Authorization");
        String ipAddress = CustomServlet.getClientIpAddress(request);
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            OrderTypeEntity existOrderType = orderTypeService.getOrderTypeByName(req.getOrder_type_name().toUpperCase());
            if( existOrderType != null ){
                resp.setCount(0);
                resp.setData(null);
                resp.setStatusCode(HttpStatus.BAD_REQUEST.value());
                resp.setMessage("Error order type name "+req.getOrder_type_name().toUpperCase()+" has already created a order type");
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
            }

            // create orderType in database
            Long orderTypeID = orderTypeService.registerOrderType(req, vsf.getUsername());
            System.out.println("registered order type id: " + orderTypeID);
            OrderTypeEntity orderTypeDetail = orderTypeService.getOrderTypeDetail(orderTypeID);
            
            // create orderType in kafka server
            TopicReq topicConfig = new TopicReq();
            topicConfig.setRetentionMs(req.getMessage_expire());
            topicConfig.setTopicName(orderTypeDetail.getOrderTypeName());
            kafkaClientService.createTopic(topicConfig);


            AuditLog auditLog = new AuditLog();
            auditLog.setAction("create");
            auditLog.setAuditable_id(orderTypeID);
            auditLog.setAuditable("ordertype");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setIp_address(ipAddress);
            auditLog.setComment("createOrderType");
            auditLog.setCreated_date(DateTime.getTimeStampNow());
            auditService.AddAuditLog(auditLog);

            LogFlie.logMessage(
                "ManageSystemController", 
                String.format("audit_logs/%s/add",LogFlie.dateFolderName()),
                String.format(
                    "%s %s %s %s %s %s %s %s %s",
                    df.format(new Date()),
                    "insert",
                    "createOrderType",
                    "ordertype",
                    vsf.getUsername(),
                    ipAddress,
                    vsf.getDevice(),
                    vsf.getBrowser(),
                    vsf.getSystem()
                )
            );

            resp.setCount(1);
            resp.setData(orderTypeDetail);
            return new ResponseEntity<>(resp, HttpStatus.CREATED);
        } catch (Exception e) {
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while updating : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/order_type")
    public ResponseEntity<DefaultControllerResp> updateOrderType(HttpServletRequest request, @RequestBody UpdateOrderTypeReq req) throws Exception{
        
        String ipAddress = CustomServlet.getClientIpAddress(request);
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);

        DefaultControllerResp response = new DefaultControllerResp();
        try{
            // update orderType in database
            OrderTypeEntity updateOrderType = orderTypeService.updateOrderType(req, vsf.getUsername());
            if( updateOrderType == null ){
                response.setCount(0);
                response.setData(null);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Error Not Found order type id "+req.getUpdateID());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
            // recreate orderType in kafka server
            if (req.getUpdateInfo().getIs_delete().equals(0)){
                TopicReq topicUpdate = new TopicReq();
                topicUpdate.setTopicName(updateOrderType.getOrderTypeName());
                topicUpdate.setRetentionMs(updateOrderType.getMESSAGE_EXPIRE());
                kafkaClientService.createTopic(topicUpdate);
            }

            // update orderType in kafka server
            if (!req.getUpdateInfo().getMessage_expire().isEmpty()){
                TopicReq topicUpdate = new TopicReq();
                topicUpdate.setTopicName(updateOrderType.getOrderTypeName());
                topicUpdate.setRetentionMs(updateOrderType.getMESSAGE_EXPIRE());
                kafkaClientService.updateTopic(topicUpdate);
            }

            AuditLog auditLog = new AuditLog();
            auditLog.setAction("update");
            auditLog.setAuditable_id(req.getUpdateID());
            auditLog.setAuditable("ordertype");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setIp_address(ipAddress);
            auditLog.setComment("updateOrderType");
            auditLog.setCreated_date(DateTime.getTimeStampNow());
            auditService.AddAuditLog(auditLog);

            LogFlie.logMessage(
                "ManageSystemController", 
                String.format("audit_logs/%s/update",LogFlie.dateFolderName()),
                String.format(
                    "%s %s %s %s %s %s %s %s %s",
                    df.format(new Date()),
                    "update",
                    "updateOrderType",
                    "ordertype",
                    vsf.getUsername(),
                    ipAddress,
                    vsf.getDevice(),
                    vsf.getBrowser(),
                    vsf.getSystem()
                )
            );


            response.setCount(1);
            response.setMessage("Success");
            response.setData(req);
            
            response.setStatusCode(200);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            response.setCount(0);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/order_type")
    public ResponseEntity<DefaultControllerResp> deleteOrderType(HttpServletRequest request, @RequestParam(name="order_type_id")Long orderTypeID) throws Exception{
        
        String ipAddress = CustomServlet.getClientIpAddress(request);
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);

        DefaultControllerResp response = new DefaultControllerResp();
        try{
            // get order_type in database
            OrderTypeEntity orderType = orderTypeService.getOrderTypeDetail(orderTypeID);
            if( orderType == null ){
                response.setCount(0);
                response.setData(null);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Error Not Found order type id "+orderTypeID);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // delete order_type in database
            orderTypeService.deleteOrderType(orderTypeID, vsf.getUsername());

            // delete topic in kafka
            if (orderType != null){
                kafkaClientService.deleteTopic(orderType.getOrderTypeName().toUpperCase());
            }
            
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("delete");
            auditLog.setAuditable_id(orderTypeID);
            auditLog.setAuditable("ordertype");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setIp_address(ipAddress);
            auditLog.setComment("deleteOrderType");
            auditLog.setCreated_date(DateTime.getTimeStampNow());
            auditService.AddAuditLog(auditLog);

            LogFlie.logMessage(
                "ManageSystemController", 
                String.format("audit_logs/%s/delete",LogFlie.dateFolderName()),
                String.format(
                    "%s %s %s %s %s %s %s %s %s",
                    df.format(new Date()),
                    "delete",
                    "deleteOrderType",
                    "ordertype",
                    vsf.getUsername(),
                    ipAddress,
                    vsf.getDevice(),
                    vsf.getBrowser(),
                    vsf.getSystem()
                )
            );
            response.setCount(1);
            response.setMessage("Success");
            
            response.setStatusCode(200);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            response.setCount(0);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/order_type/purge")
    public ResponseEntity<DefaultControllerResp> PurgeOrderTypeData(HttpServletRequest request, @RequestParam(name="order_type_id")Long orderTypeID, @RequestBody String reMark) {
        String requestHeader = request.getHeader("Authorization");
        String ipAddress = CustomServlet.getClientIpAddress(request);
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            OrderTypeEntity orderTypeData = orderTypeService.getOrderTypeDetail(orderTypeID);
            if( orderTypeData == null ){
                resp.setCount(0);
                resp.setData(null);
                resp.setStatusCode(HttpStatus.NOT_FOUND.value());
                resp.setMessage("Error Not Found order type id "+orderTypeID);
                return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
            }

            kafkaClientService.purgeDataInTopic(orderTypeData.getOrderTypeName());

            AuditLog auditLog = new AuditLog();
            auditLog.setAction("purge");
            auditLog.setAuditable_id(orderTypeID);
            auditLog.setAuditable("ordertype");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setIp_address(ipAddress);
            auditLog.setComment("PurgeOrderTypeData");
            auditLog.setCreated_date(DateTime.getTimeStampNow());
            auditService.AddAuditLog(auditLog);

            LogFlie.logMessage(
                "ManageSystemController", 
                String.format("audit_logs/%s/purge",LogFlie.dateFolderName()),
                String.format(
                    "%s %s %s %s %s %s %s %s",
                    df.format(new Date()),
                    orderTypeData.getOrderTypeName(),
                    vsf.getUsername(),
                    ipAddress,
                    vsf.getDevice(),
                    vsf.getBrowser(),
                    vsf.getSystem(),
                    reMark
                )
            );

            
            resp.setMessage("Purge all messages in order type "+orderTypeData.getOrderTypeName());

            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while purge : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/order_type/by_id")
    public ResponseEntity<Object> GetOrderTypeMoreDetail(
        @RequestBody OrderTypeMoreDetailReq req,
        HttpServletRequest request
    ) {
        try{
            
            
            String ipAddress = CustomServlet.getClientIpAddress(request);
            String requestHeader = request.getHeader("Authorization");
                
            VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);

            OrderTypeEntity ordertypeData = orderTypeService.getOrderTypeDetail(req.getOrdertype_id());

            AuditLog auditLog = new AuditLog();
            auditLog.setAction("get");
            auditLog.setAuditable("ordertype");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setAuditable_id(req.getOrdertype_id());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setIp_address(ipAddress);
            auditLog.setComment("GetOrderTypeMoreDetail");
            auditLog.setCreated_date(DateTime.getTimeStampNow());
            auditService.AddAuditLog(auditLog);
            
            DefaultControllerResp response = new DefaultControllerResp();
            
            if (ordertypeData == null) {
                response.setCount(0);
                response.setRecordsFiltered(0);
                response.setRecordsTotal(0);
                response.setMessage("Not Found");
                response.setStatusCode(404);
                return new ResponseEntity<>( response, HttpStatus.NOT_FOUND);
            } else {
                response.setCount(1);
                response.setRecordsFiltered(1);
                response.setRecordsTotal(1);
                response.setMessage("Success");
                response.setData(ordertypeData);
                response.setStatusCode(200);
                return new ResponseEntity<>( response, HttpStatus.OK);
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());
        }
    }
    
    
    @PostMapping("/consumer")
    public ResponseEntity<DefaultControllerResp> createConsumer(HttpServletRequest request, @RequestBody AddConsumerReq req) {
        String requestHeader = request.getHeader("Authorization");
        String ipAddress = CustomServlet.getClientIpAddress(request);
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            ConsumerEntity existConsumer = consumerService.getConsumerByUsername(req.getUsername());
            if( existConsumer != null ){
                resp.setCount(0);
                resp.setData(null);
                resp.setStatusCode(HttpStatus.BAD_REQUEST.value());
                resp.setMessage("Error username "+req.getUsername()+" has already created a consumer");
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
            }

            Long consumerID = consumerService.registerConsumer(req, vsf.getUsername());
            String consumerGroup = req.getSystem_name().toUpperCase();
            req.setConsumer_group(consumerGroup);
            System.out.println("registered consumer id: " + consumerID);
            ConsumerEntity consumerDetail = consumerService.consumerDetail(consumerID);
            
            // create consumer in kafka server
            kafkaClientService.createUser(req.getUsername(), req.getPassword());

            // create consumer order type
            if (req.getOrder_type_ids() != null){
                List<String> orderTypeTopicNames = new ArrayList<String>();
                List<OrderTypeEntity> listOrderTypes = orderTypeService.ListOrderTypeByIDs(req.getOrder_type_ids());
                for(OrderTypeEntity orderTypeInfo : listOrderTypes){
                    orderTypeTopicNames.add(orderTypeInfo.getOrderTypeName());
                }

                // in database
                for(Long orderTypeID : req.getOrder_type_ids()){
                    consumerOrderTypeService.registerConsumerOrderType(consumerID, orderTypeID, vsf.getUsername());
                }

                // in kafka
                List<UserAclsInfo> userAclsTopics = kafkaClientService.initUserAclsTopicList(consumerDetail.getUsername(), orderTypeTopicNames);
                kafkaClientService.createAcls(consumerDetail.getUsername(), userAclsTopics, consumerGroup);

            }

            
            
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("create");
            auditLog.setAuditable_id(consumerID);
            auditLog.setAuditable("consumer");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setIp_address(ipAddress);
            auditLog.setComment("createConsumer");
            auditLog.setCreated_date(DateTime.getTimeStampNow());
            auditService.AddAuditLog(auditLog);


            LogFlie.logMessage(
                "ManageSystemController", 
                String.format("audit_logs/%s/add",LogFlie.dateFolderName()),
                String.format(
                    "%s %s %s %s %s %s %s %s %s",
                    df.format(new Date()),
                    "insert",
                    "createConsumer",
                    "consumer",
                    vsf.getUsername(),
                    ipAddress,
                    vsf.getDevice(),
                    vsf.getBrowser(),
                    vsf.getSystem()
                )
            );

            resp.setCount(1);
            resp.setData(consumerDetail);
            return new ResponseEntity<>(resp, HttpStatus.CREATED);
        } catch (Exception e) {
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while updating : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/consumer")
    public ResponseEntity<DefaultControllerResp> updateConsumer(HttpServletRequest request, @RequestBody UpdateByConsumerReq req) throws Exception{
        
        String ipAddress = CustomServlet.getClientIpAddress(request);
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);

        DefaultControllerResp response = new DefaultControllerResp();
        try{
            // update consumer in database
            ConsumerEntity updateConsumer = consumerService.updateConsumer(req, vsf.getUsername());

            if( updateConsumer == null ){
                response.setCount(0);
                response.setData(null);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Error Not Found consumer id "+req.getUpdateID());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            String consumerGroup = updateConsumer.getSystem_name().toUpperCase();

            // update consumer in kafka server
            List<UserAclsInfo> userAcls = kafkaClientService.ListUserAcls(updateConsumer.getUsername());
            if(req.getUpdateInfo().getPassword()!=null){
                if (!req.getUpdateInfo().getPassword().isEmpty() || req.getUpdateInfo().getPassword().isBlank()){
                    kafkaClientService.deleteUserAndAcls(updateConsumer.getUsername(), userAcls);
                    kafkaClientService.createUserAndAcls(updateConsumer.getUsername(), req.getUpdateInfo().getPassword(),userAcls,consumerGroup );
                }
            }

            if(req.getUpdateInfo().getIs_enable()!=null){
                if(req.getUpdateInfo().getIs_enable().equals(0)){
                    kafkaClientService.deleteAcls(updateConsumer.getUsername(), userAcls);
                } else if(req.getUpdateInfo().getIs_enable().equals(1)){
                    List<ConsumerLJoinOrderType> consumerOrderTypes = consumerOrderTypeService.ListConsumerOrderType(updateConsumer.getID());
                    List<String> orderTypeTopicNames = new ArrayList<>();
                    for (ConsumerLJoinOrderType consumerOrderType : consumerOrderTypes){
                        orderTypeTopicNames.add(consumerOrderType.getORDERTYPE_NAME());
                    }
                    List<UserAclsInfo> userAclsTopics = kafkaClientService.initUserAclsTopicList(updateConsumer.getUsername(), orderTypeTopicNames);
                    kafkaClientService.createAcls(updateConsumer.getUsername(), userAclsTopics, consumerGroup.toUpperCase());
                }
            }


            if ( req.getUpdateInfo().getOrder_type_ids() != null ){
                Error err = consumerOrderTypeService.updateConsumerOrderType(updateConsumer.getID(), req.getUpdateInfo().getOrder_type_ids(), vsf.getUsername());
                if(err != null){
                    if (err.getMessage() != null){
                        response.setCount(0);
                        response.setData(err.getMessage());
                        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                        response.setMessage("Error update Consumer and orderType ");
                        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                    }
                }
                
                if(updateConsumer.getIs_enable().equals(1)){
                    List<ConsumerLJoinOrderType> consumerOrderTypes = consumerOrderTypeService.ListConsumerOrderType(updateConsumer.getID());
                    List<String> orderTypeTopicNames = new ArrayList<>();
                    for (ConsumerLJoinOrderType consumerOrderType : consumerOrderTypes){
                        orderTypeTopicNames.add(consumerOrderType.getORDERTYPE_NAME());
                    }
                    List<UserAclsInfo> userAclsTopics = kafkaClientService.initUserAclsTopicList(updateConsumer.getUsername(), orderTypeTopicNames);
                    kafkaClientService.createAcls(req.getUpdateInfo().getUsername(), userAclsTopics, consumerGroup);
                }
            }

            AuditLog auditLog = new AuditLog();
            auditLog.setAction("update");
            auditLog.setAuditable_id(req.getUpdateID());
            auditLog.setAuditable("consumer");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setIp_address(ipAddress);
            auditLog.setComment("updateConsumer");
            auditLog.setCreated_date(DateTime.getTimeStampNow());
            auditService.AddAuditLog(auditLog);


            LogFlie.logMessage(
                "ManageSystemController", 
                String.format("audit_logs/%s/update",LogFlie.dateFolderName()),
                String.format(
                    "%s %s %s %s %s %s %s %s %s",
                    df.format(new Date()),
                    "update",
                    "updateConsumer",
                    "consumer",
                    vsf.getUsername(),
                    ipAddress,
                    vsf.getDevice(),
                    vsf.getBrowser(),
                    vsf.getSystem()
                )
            );


            response.setCount(1);
            response.setMessage("Success");
            response.setData(req);
            
            response.setStatusCode(200);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            response.setCount(0);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/consumer")
    public ResponseEntity<DefaultControllerResp> updateConsumer(HttpServletRequest request, @RequestParam(name="consumer_id")Long consumerID) throws Exception{
        
        String ipAddress = CustomServlet.getClientIpAddress(request);
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);

        DefaultControllerResp response = new DefaultControllerResp();
        try{
            // get consumer in database
            ConsumerEntity consumer = consumerService.consumerDetail(consumerID);
            if( consumer == null ){
                response.setCount(0);
                response.setData(null);
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Error Not Found consumer id "+consumerID);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // delete consumer in database
            consumerService.deleteConsumer(consumerID, vsf.getUsername());

            // delete consumer in database
            List<Long> clearList = new ArrayList<Long>();
            consumerOrderTypeService.updateConsumerOrderType(consumerID, clearList, vsf.getUsername());

            // list consumer acls in kafka
            List<UserAclsInfo> userAcls = kafkaClientService.ListUserAcls(consumer.getUsername());

            // delete consumer in kafka
            kafkaClientService.deleteUserAndAcls(consumer.getUsername(), userAcls);
            
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("delete");
            auditLog.setAuditable_id(consumerID);
            auditLog.setAuditable("consumer");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setIp_address(ipAddress);
            auditLog.setComment("deleteConsumer");
            auditLog.setCreated_date(DateTime.getTimeStampNow());
            auditService.AddAuditLog(auditLog);

            LogFlie.logMessage(
                "ManageSystemController", 
                String.format("audit_logs/%s/update",LogFlie.dateFolderName()),
                String.format(
                    "%s %s %s %s %s %s %s %s %s",
                    df.format(new Date()),
                    "delete",
                    "deleteConsumer",
                    "consumer",
                    vsf.getUsername(),
                    ipAddress,
                    vsf.getDevice(),
                    vsf.getBrowser(),
                    vsf.getSystem()
                )
            );

            response.setCount(1);
            response.setMessage("Success");
            
            response.setStatusCode(200);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            response.setCount(0);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/consumers")
    public ResponseEntity<DefaultControllerResp> getManageConsumers(
        HttpServletRequest request,    
        @RequestParam(name = "draw", defaultValue = "11")Integer draw,
        @RequestParam(name = "order[0][dir]", defaultValue = "ASC")String sortBy,
        @RequestParam(name = "order[0][name]", defaultValue = "created_date")String sortName,
        @RequestParam(name = "start_time" , defaultValue = "")String startTime,
        @RequestParam(name = "end_time" , defaultValue = "")String endTime,
        @RequestParam(name = "start", defaultValue = "0")Integer start,
        @RequestParam(name = "length", defaultValue = "10")Integer length,
        @RequestParam(name = "Search", defaultValue = "")String search,
        @RequestParam(name = "Search_field", defaultValue = "")String searchField
    ){
        
        DefaultControllerResp resp = new DefaultControllerResp();
        String ipAddress = CustomServlet.getClientIpAddress(request);
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        try {
            ListConsumerReq req = new ListConsumerReq(
                draw,
                sortBy,
                sortName,
                start,
                length,
                search,
                searchField
            );

            // System.out.println("doing ... ");

            PaginationDataResp listConsumers = manageSystemService.ListManageConsumers(req);

            // Collection<String> topics = Collections.singletonList("PACKAGE_EXPIRE");
            // String groupId = "sms_module.worker.test";
            // System.out.println("counts: " + listConsumers.getCount());
            // System.out.println("data: " + listConsumers.getData().toString());
            // kafkaClientService.calculateConsumerLag(groupId, topics);
            
            resp.setDraw(draw);
            resp.setCount(listConsumers.getCount());
            resp.setData(listConsumers.getData());
            resp.setRecordsFiltered(listConsumers.getCount());
            resp.setRecordsTotal(listConsumers.getCount());
            resp.setStatusCode(HttpStatus.OK.value());
            resp.setMessage("Successfully");

            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setDraw(draw);
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while get detail : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/consumer")
    public ResponseEntity<DefaultControllerResp> getManageConsumerByID(
        HttpServletRequest request,
        @RequestParam(name = "consumer_id")Long consumerID
    ){
        String ipAddress = CustomServlet.getClientIpAddress(request);
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            ConsumerEntity consumerDetail = consumerService.consumerDetail(consumerID);

            List<ConsumerLJoinOrderType> cod = consumerOrderTypeService.ListConsumerOrderType(consumerDetail.getID());

            if (consumerDetail != null) {
                ConsumerODTDetailResp condt = new ConsumerODTDetailResp();
                condt.setId(consumerDetail.getID());
                condt.setConsumer_group(consumerDetail.getConsumer_group());
                condt.setCreated_by(consumerDetail.getCreated_by());
                condt.setCreated_date(consumerDetail.getCreated_date());
                condt.setDepartmentName(consumerDetail.getDepartmentName());
                condt.setEmail(consumerDetail.getEmail());
                condt.setIs_delete(consumerDetail.getIs_delete());
                condt.setIs_delete_by(consumerDetail.getIs_delete_by());
                condt.setIs_enable(consumerDetail.getIs_enable());
                condt.setUsername(consumerDetail.getUsername());
                condt.setPhoneNumber(consumerDetail.getPhoneNumber());
                condt.setSystem_name(consumerDetail.getSystem_name());
                condt.setUpdated_by(consumerDetail.getUpdated_by());
                condt.setContactName(consumerDetail.getContactName());
                condt.setUpdated_date(consumerDetail.getUpdated_date());
                condt.setOrderTypes(cod);
                resp.setRecordsFiltered(1);
                resp.setRecordsTotal(1);
                resp.setCount(1);
                resp.setData(condt);
                resp.setStatusCode(HttpStatus.OK.value());
                resp.setMessage("Successfully");
                return new ResponseEntity<>( resp, HttpStatus.OK);
            }else{
                resp.setRecordsFiltered(0);
                resp.setRecordsTotal(0);
                resp.setCount(0);
                resp.setStatusCode(HttpStatus.NOT_FOUND.value());
                resp.setMessage("NOTFOUND");
            }
            return new ResponseEntity<>( resp, HttpStatus.NOT_FOUND);
            
        }catch (Exception e){
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while getting : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/metric")
    public ResponseEntity<DefaultControllerResp> createManageMetric(HttpServletRequest request, @RequestBody AddMetricNotificationReq req) {
        String requestHeader = request.getHeader("Authorization");
        String ipAddress = CustomServlet.getClientIpAddress(request);
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            Long metricNotificationID = saMetricNotificationService.registerMetricNotification(req, vsf.getUsername());
            SaMetricNotificationEntity saMetricNotificationDetail = saMetricNotificationService.saMetricNotificationDetail(metricNotificationID);
            resp.setCount(1);
            resp.setData(saMetricNotificationDetail);
            return new ResponseEntity<>(resp, HttpStatus.CREATED);
        } catch (Exception e) {
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while creating : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/metric")
    public ResponseEntity<DefaultControllerResp> updateManageMetric(HttpServletRequest request, @RequestBody UpdateMetricReq req) throws Exception{
        
        String ipAddress = CustomServlet.getClientIpAddress(request);
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);

        DefaultControllerResp response = new DefaultControllerResp();
        try{
            SaMetricNotificationEntity saMetric = saMetricNotificationService.updateMetricNotification(req, vsf.getUsername());

            response.setCount(1);
            response.setMessage("Success");
            response.setData(saMetric);
            
            response.setStatusCode(200);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            response.setCount(0);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/metrics")
    public ResponseEntity<DefaultControllerResp> getManageMetrics(
        HttpServletRequest request,
        @RequestParam(name = "draw", defaultValue = "11")Integer draw,
        @RequestParam(name = "order[0][dir]", defaultValue = "ASC")String sortBy,
        @RequestParam(name = "order[0][name]", defaultValue = "updated_date")String sortName,
        @RequestParam(name = "start_time" , defaultValue = "")String startTime,
        @RequestParam(name = "end_time" , defaultValue = "")String endTime,
        @RequestParam(name = "start", defaultValue = "0")Integer start,
        @RequestParam(name = "length", defaultValue = "10")Integer length,
        @RequestParam(name = "Search", defaultValue = "")String search,
        @RequestParam(name = "Search_field", defaultValue = "")String searchField
    ) {
        String requestHeader = request.getHeader("Authorization");
        String ipAddress = CustomServlet.getClientIpAddress(request);
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            DefaultListReq req = new DefaultListReq(
                draw,
                sortBy,
                sortName,
                startTime,
                endTime,
                start,
                length,
                search,
                searchField
            );

            PaginationDataResp saMetrics = manageSystemService.ListManageMetrics(req);
            resp.setDraw(draw);
            resp.setRecordsFiltered(saMetrics.getCount());
            resp.setRecordsTotal(saMetrics.getCount());
            resp.setCount(saMetrics.getCount());
            resp.setData(saMetrics.getData());
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            resp.setDraw(draw);
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while getting : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/metrics/notification_messages")
    public ResponseEntity<DefaultControllerResp> getManageMetricNotificationMessages(
        HttpServletRequest request,
        @RequestParam(name = "draw", defaultValue = "11")Integer draw,
        @RequestParam(name = "order[0][dir]", defaultValue = "ASC")String sortBy,
        @RequestParam(name = "order[0][name]", defaultValue = "created_date")String sortName,
        @RequestParam(name = "start_time" , defaultValue = "")String startTime,
        @RequestParam(name = "end_time" , defaultValue = "")String endTime,
        @RequestParam(name = "start", defaultValue = "0")Integer start,
        @RequestParam(name = "length", defaultValue = "10")Integer length,
        @RequestParam(name = "Search", defaultValue = "")String search,
        @RequestParam(name = "Search_field", defaultValue = "")String searchField
    ) {
        String requestHeader = request.getHeader("Authorization");
        String ipAddress = CustomServlet.getClientIpAddress(request);
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            DefaultListReq req = new DefaultListReq(
                draw,
                sortBy,
                sortName,
                startTime,
                endTime,
                start,
                length,
                search,
                searchField
            );

            PaginationDataResp notifications = notificationMsgService.ListMetricNotificationMessages(req);
            resp.setDraw(draw);
            resp.setRecordsFiltered(notifications.getCount());
            resp.setRecordsTotal(notifications.getCount());
            resp.setCount(notifications.getCount());
            resp.setData(notifications.getData());
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            resp.setDraw(draw);
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while creating : " + e.getMessage());
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
            ObjectMapper mapper = new ObjectMapper();

            String objectString = mapper.writeValueAsString(data.getMessage());

            String err = kafkaClientService.consumerPublishMessage(
                "admin",
                "admin-secret",
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


    @GetMapping("/queues")
    public ResponseEntity<Object> getManageQueues(
        HttpServletRequest request,    
        @RequestParam(name = "draw", defaultValue = "11")Integer draw,
        @RequestParam(name = "order[0][dir]", defaultValue = "ASC")String sortBy,
        @RequestParam(name = "order[0][name]", defaultValue = "created_date")String sortName,
        @RequestParam(name = "start_time" , defaultValue = "")String startTime,
        @RequestParam(name = "end_time" , defaultValue = "")String endTime,
        @RequestParam(name = "start", defaultValue = "0")Integer start,
        @RequestParam(name = "length", defaultValue = "10")Integer length,
        @RequestParam(name = "Search", defaultValue = "")String search,
        @RequestParam(name = "Search_field", defaultValue = "")String searchField
    ){
        
        DefaultControllerResp resp = new DefaultControllerResp();
        String ipAddress = CustomServlet.getClientIpAddress(request);
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        try {

            List<HashMap<String, Object>> data = kafkaUIService.GetConsumerGroup();
            HashMap<String, Object> dataResp = new HashMap<String, Object>();
            dataResp.put("pageCount", 1);
            dataResp.put("consumerGroups", data);

            return new ResponseEntity<>( dataResp, HttpStatus.OK);
        }catch (Exception e){
            resp.setDraw(draw);
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while get detail : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/message_behinds")
    public ResponseEntity<Object> getConsumerMessageBehinds(
        HttpServletRequest request,    
        @RequestParam(name = "topic_name")String topic,
        @RequestParam(name = "consumer_id")Long consumerID,
        @RequestParam(name = "offset" , defaultValue = "-1")Integer offset,
        @RequestParam(name = "limit", defaultValue = "10")Integer limit,
        @RequestParam(name = "is_enable_auto_commit" , defaultValue = "false")Boolean isEnableAutoCommit
    ){
        
        DefaultControllerResp resp = new DefaultControllerResp();
        String ipAddress = CustomServlet.getClientIpAddress(request);
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        try {
            List<ConsumeMessage> listBehindMessages = new ArrayList<ConsumeMessage>();
            ConsumerEntity con = consumerService.consumerDetail(consumerID);
            HashMap<String, Object> behindMaps = kafkaClientService.countMessageBehindByTopic(topic, con.getConsumer_group());
            for (String topicName: behindMaps.keySet()) {
                Object value = behindMaps.get(topicName);
                try {
                    // Convert the value to a JSON string
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonString = mapper.writeValueAsString(value);

                    // Convert the JSON string to a JSONArray
                    JSONArray listBehind = new JSONArray(jsonString);

                    // Print the JSONArray (or use it as needed)
                    // System.out.println("Topic: " + topicName);
                    // System.out.println("JSONArray: " + listBehind.toString());
                    for (int i = 0; i < listBehind.length(); i++){
                        JSONObject behind = listBehind.getJSONObject(i);
                        Integer behindLimit = behind.getInt("limit");
                        Integer beginOffset = behind.getInt("currentOffset");

                        if(beginOffset < offset && !offset.equals(-1)){
                            continue;
                        }

                        ListConsumeMsg consumeMsg = kafkaClientService.consumeMessagesAndNack(topicName, con.getConsumer_group(), beginOffset, behindLimit, isEnableAutoCommit);
                        if(consumeMsg.getErr()== null){
                            listBehindMessages.addAll(consumeMsg.getMessages());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }   
            
            if(listBehindMessages.size() <= limit){
                return new ResponseEntity<>(listBehindMessages, HttpStatus.OK);
            }

            return new ResponseEntity<>(listBehindMessages.subList(0, limit), HttpStatus.OK);
        }catch (Exception e){
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while get detail : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/topic_behinds")
    public ResponseEntity<Object> getTopicByConsumerMessageBehinds(
        HttpServletRequest request,    
        @RequestParam(name = "consumer_id")Long consumerID
    ){
        
        DefaultResp resp = new DefaultResp();
        String ipAddress = CustomServlet.getClientIpAddress(request);
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        try {
            String orderTypeTopicNames = "";
            ConsumerEntity con = consumerService.consumerDetail(consumerID);
            if(con == null){
                resp.setMessage("NOT FOUND CONSUMER ID:" + consumerID);
                return new ResponseEntity<>( resp, HttpStatus.NOT_FOUND);
            }

            try{
                List<ConsumerLJoinOrderType> orderCons = consumerOrderTypeService.ListConsumerOrderType(consumerID);
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


            TopicDetailResp data = kafkaClientService.getTopicDescriptionByConsumerAdmin(
                con.getConsumer_group(),
                orderTypeTopicNames
            );
            if (data.getError() != null){
                resp.setError(data.getError());
                resp.setMessage("Error while topic_detail : " + data.getError());
                return new ResponseEntity<>( resp, HttpStatus.BAD_REQUEST);
            }
            try{
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
            resp.setMessage("Error while get detail : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
