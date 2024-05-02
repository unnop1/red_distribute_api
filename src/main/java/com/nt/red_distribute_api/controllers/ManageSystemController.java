package com.nt.red_distribute_api.controllers;

import com.nt.red_distribute_api.Auth.JwtHelper;
import com.nt.red_distribute_api.Util.DateTime;
import com.nt.red_distribute_api.dto.req.audit.AuditLog;
import com.nt.red_distribute_api.dto.req.consumer.AddConsumerReq;
import com.nt.red_distribute_api.dto.req.consumer.UpdateByConsumerReq;
import com.nt.red_distribute_api.dto.req.manage_system.ListByOrderTypeReq;
import com.nt.red_distribute_api.dto.req.sa_metric_notification.AddMetricNotificationReq;
import com.nt.red_distribute_api.dto.req.sa_metric_notification.UpdateMetricReq;
import com.nt.red_distribute_api.dto.resp.DefaultControllerResp;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.dto.resp.VerifyAuthResp;
import com.nt.red_distribute_api.entity.ConsumerEntity;
import com.nt.red_distribute_api.entity.SaMetricNotificationEntity;
import com.nt.red_distribute_api.service.AuditService;
import com.nt.red_distribute_api.service.ConsumerService;
import com.nt.red_distribute_api.service.ManageSystemService;
import com.nt.red_distribute_api.service.SaMetricNotificationService;

import jakarta.servlet.http.HttpServletRequest;

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
    private ManageSystemService manageSystemService;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private SaMetricNotificationService saMetricNotificationService;

    @GetMapping("/order_types")
    public ResponseEntity<DefaultControllerResp> getManageOrderTypes(
        HttpServletRequest request,    
        @RequestParam(name = "page", defaultValue = "1")Integer page,
        @RequestParam(name = "limit", defaultValue = "10")Integer limit
    ){
        
        DefaultControllerResp resp = new DefaultControllerResp();
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        try {
            PaginationDataResp manageOrderTypes = manageSystemService.ListManageOrderTypes(page, limit);
            
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("update");
            auditLog.setAuditable("");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setIp_address(ipAddress);
            auditLog.setComment("getManageOrderTypes");
            auditLog.setCreated_date(DateTime.getTimeStampNow());
            auditService.AddAuditLog(auditLog);
            
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

    @GetMapping("/order_types/list")
    public ResponseEntity<PaginationDataResp> getAllManageSystem(
        HttpServletRequest request,    
        @RequestParam(name = "draw", defaultValue = "11")Integer draw,
        @RequestParam(name = "order[0][dir]", defaultValue = "ASC")String sortBy,
        @RequestParam(name = "order[0][name]", defaultValue = "created_date")String sortName,
        @RequestParam(name = "start_time")String startTime,
        @RequestParam(name = "end_time")String endTime,
        @RequestParam(name = "start", defaultValue = "0")Integer start,
        @RequestParam(name = "length", defaultValue = "10")Integer length,
        @RequestParam(name = "Search", defaultValue = "")String search,
        @RequestParam(name = "Search_field", defaultValue = "")String searchField,
        @RequestParam(name = "order_type")String byOrderType
    ){
        
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);

        ListByOrderTypeReq req = new ListByOrderTypeReq(
            draw,
            sortBy,
            sortName,
            startTime,
            endTime,
            start,
            length,
            search,
            searchField,
            byOrderType
        );

        AuditLog auditLog = new AuditLog();
        auditLog.setAction("get");
        auditLog.setAuditable("trigger");
        auditLog.setUsername(vsf.getUsername());
        auditLog.setBrowser(vsf.getBrowser());
        auditLog.setDevice(vsf.getDevice());
        auditLog.setOperating_system(vsf.getSystem());
        auditLog.setIp_address(ipAddress);
        auditLog.setComment("getAllTriggers");
        auditLog.setCreated_date(DateTime.getTimeStampNow());
        auditService.AddAuditLog(auditLog);
        
        return new ResponseEntity<>( manageSystemService.ListManageByOrderType(req), HttpStatus.OK);
    }

    @PostMapping("/consumer")
    public ResponseEntity<DefaultControllerResp> createConsumer(HttpServletRequest request, @RequestBody AddConsumerReq req) {
        String requestHeader = request.getHeader("Authorization");
        String ipAddress = request.getRemoteAddr();
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            Long consumerID = consumerService.registerConsumer(req, vsf.getUsername());
            ConsumerEntity consumerDetail = consumerService.consumerDetail(consumerID);
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
        
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);

        DefaultControllerResp response = new DefaultControllerResp();
        try{
            consumerService.updateConsumer(req, vsf.getUsername());
            

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
        
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);

        DefaultControllerResp response = new DefaultControllerResp();
        try{
            consumerService.deleteConsumer(consumerID, vsf.getUsername());
            

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
    public ResponseEntity<DefaultControllerResp> getManageOrderTypes(
        HttpServletRequest request,    
        @RequestParam(name = "consumer", defaultValue = "1")Long consumerID
    ){
        
        DefaultControllerResp resp = new DefaultControllerResp();
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        try {
            ConsumerEntity consumerDetail = consumerService.consumerDetail(consumerID);
            
            resp.setCount(1);
            resp.setData(consumerDetail);
            resp.setStatusCode(HttpStatus.OK.value());
            resp.setMessage("Successfully");

            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while get detail : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/metric")
    public ResponseEntity<DefaultControllerResp> createManageMetric(HttpServletRequest request, @RequestBody AddMetricNotificationReq req) {
        String requestHeader = request.getHeader("Authorization");
        String ipAddress = request.getRemoteAddr();
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            Long metricNotificationID = saMetricNotificationService.registerMetricNotification(req, vsf.getUsername());
            ConsumerEntity saMetricNotificationDetail = consumerService.consumerDetail(metricNotificationID);
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
        
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);

        DefaultControllerResp response = new DefaultControllerResp();
        try{
            saMetricNotificationService.updateMetricNotification(req, vsf.getUsername());
            

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


    @GetMapping("/metrics")
    public ResponseEntity<DefaultControllerResp> getManageMetric(HttpServletRequest request, @RequestParam(name="metric_notification_id")Long metricNotificationID) {
        String requestHeader = request.getHeader("Authorization");
        String ipAddress = request.getRemoteAddr();
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            SaMetricNotificationEntity saMetricNotificationDetail = saMetricNotificationService.saMetricNotificationDetail(metricNotificationID);
            resp.setCount(1);
            resp.setData(saMetricNotificationDetail);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while creating : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
