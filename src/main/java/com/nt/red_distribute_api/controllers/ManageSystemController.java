package com.nt.red_distribute_api.controllers;

import com.nt.red_distribute_api.Auth.JwtHelper;
import com.nt.red_distribute_api.Util.DateTime;
import com.nt.red_distribute_api.dto.req.audit.AuditLog;
import com.nt.red_distribute_api.dto.req.manage_system.ListByOrderTypeReq;
import com.nt.red_distribute_api.dto.req.ordertype.ListOrderTypeReq;
import com.nt.red_distribute_api.dto.req.trigger.ListTriggerReq;
import com.nt.red_distribute_api.dto.resp.DefaultControllerResp;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.dto.resp.VerifyAuthResp;
import com.nt.red_distribute_api.entity.AuditLogEntity;
import com.nt.red_distribute_api.entity.PermissionMenuEntity;
import com.nt.red_distribute_api.entity.TriggerMessageEntity;
import com.nt.red_distribute_api.service.AuditService;
import com.nt.red_distribute_api.service.ManageSystemService;
import com.nt.red_distribute_api.service.TriggerMessageService;

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
            resp.setMessage("Successfully updated");

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


}
