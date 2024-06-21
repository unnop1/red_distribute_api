package com.nt.red_distribute_api.controllers;

import com.nt.red_distribute_api.Auth.JwtHelper;
import com.nt.red_distribute_api.Util.DateTime;
import com.nt.red_distribute_api.dto.req.audit.AuditLog;
import com.nt.red_distribute_api.dto.req.trigger.DashboardReq;
import com.nt.red_distribute_api.dto.req.trigger.ListTriggerReq;
import com.nt.red_distribute_api.dto.resp.DefaultControllerResp;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.dto.resp.VerifyAuthResp;
import com.nt.red_distribute_api.entity.TriggerMessageEntity;
import com.nt.red_distribute_api.entity.view.trigger.TriggerOrderTypeCount;
import com.nt.red_distribute_api.service.AuditService;
import com.nt.red_distribute_api.service.TriggerMessageService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/trigger")
public class TriggerController {

    @Autowired
    private JwtHelper helper;

    @Autowired
    private AuditService auditService;

    @Autowired
    private TriggerMessageService triggerService;

    @GetMapping("/dashboard")
    public ResponseEntity<DefaultControllerResp> getDashBoardTriggers(
        HttpServletRequest request,    
        @RequestParam(name = "draw", defaultValue = "11")Integer draw,
        @RequestParam(name = "order[0][dir]", defaultValue = "ASC")String sortBy,
        @RequestParam(name = "order[0][name]", defaultValue = "created_date")String sortName,
        @RequestParam(name = "start_time", defaultValue = "")String startTime,
        @RequestParam(name = "end_time", defaultValue = "")String endTime,
        @RequestParam(name = "channel_id", defaultValue = "0")Long channelID
    ){
        
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            DashboardReq req = new DashboardReq(
                channelID, sortBy, sortName, startTime, endTime
            );

            PaginationDataResp triggers = triggerService.Dashboard(req);
            resp.setDraw(draw);
            resp.setStatusCode(HttpStatus.OK.value());
            resp.setCount(triggers.getCount());
            resp.setData(triggers.getData());
            resp.setRecordsFiltered(triggers.getCount());
            resp.setRecordsTotal(triggers.getCount());
            
            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setDraw(draw);
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while getting : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/by_order_type")
    public ResponseEntity<DefaultControllerResp> getAllTriggersByOrderType(
        HttpServletRequest request,    
        @RequestParam(name = "draw", defaultValue = "11")Integer draw,
        @RequestParam(name = "order[0][dir]", defaultValue = "ASC")String sortBy,
        @RequestParam(name = "order[0][name]", defaultValue = "orderid")String sortName,
        @RequestParam(name = "start_time", defaultValue = "")String startTime,
        @RequestParam(name = "end_time", defaultValue = "")String endTime,
        @RequestParam(name = "start", defaultValue = "0")Integer start,
        @RequestParam(name = "length", defaultValue = "10")Integer length,
        @RequestParam(name = "Search", defaultValue = "")String search,
        @RequestParam(name = "Search_field", defaultValue = "")String searchField,
        @RequestParam(name = "order_type_id")Long orderTypeID
    ){
        
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            ListTriggerReq req = new ListTriggerReq(
                sortBy,
                sortName,
                startTime,
                endTime,
                start,
                length,
                orderTypeID,
                search,
                searchField
            );

            PaginationDataResp triggers = triggerService.ListAllTrigger(req);
            resp.setDraw(draw);
            resp.setStatusCode(HttpStatus.OK.value());
            resp.setCount(triggers.getCount());
            resp.setData(triggers.getData());
            resp.setRecordsFiltered(triggers.getCount());
            resp.setRecordsTotal(triggers.getCount());

            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setDraw(draw);
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while getting : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/detail")
    public ResponseEntity<DefaultControllerResp> getTriggerDetail(
        HttpServletRequest request,
        @RequestParam(name = "trigger_id")Long triggerID
    ){
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            TriggerMessageEntity triggerMsg = triggerService.TriggerDetail(triggerID);

            if (triggerMsg != null) {
                resp.setRecordsFiltered(1);
                resp.setRecordsTotal(1);
                resp.setCount(1);
                resp.setData(triggerMsg);
                resp.setStatusCode(HttpStatus.OK.value());
                resp.setMessage("Successfully");
                return new ResponseEntity<>( resp, HttpStatus.OK);
            }else{
                resp.setCount(0);
                resp.setData(null);
                resp.setStatusCode(HttpStatus.NOT_FOUND.value());
                resp.setMessage("Not found");
                return new ResponseEntity<>( resp, HttpStatus.NOT_FOUND);
            }
            
        }catch (Exception e){
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while getting : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count/by_ordertype")
    public ResponseEntity<DefaultControllerResp> getTriggerOrderTypeCount(
        HttpServletRequest request
    ){
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        DefaultControllerResp resp = new DefaultControllerResp();

        try {
            List<TriggerOrderTypeCount> triggerCountMsg = triggerService.CountTriggerAllOrderType();
            resp.setCount(triggerCountMsg.size());
            resp.setData(triggerCountMsg);
            return new ResponseEntity<>( resp, HttpStatus.OK);
            
        }catch (Exception e){
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while getting : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
