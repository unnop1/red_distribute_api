package com.nt.red_distribute_api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.red_distribute_api.Auth.JwtHelper;
import com.nt.red_distribute_api.Util.DateTime;
import com.nt.red_distribute_api.dto.req.audit.AuditLog;
import com.nt.red_distribute_api.dto.req.permission.AddPermissionReq;
import com.nt.red_distribute_api.dto.req.permission.PermissionListReq;
import com.nt.red_distribute_api.dto.req.permission.UpdateByPermissionReq;
import com.nt.red_distribute_api.dto.resp.DefaultControllerResp;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.dto.resp.VerifyAuthResp;
import com.nt.red_distribute_api.entity.PermissionMenuEntity;
import com.nt.red_distribute_api.log.LogFlie;
import com.nt.red_distribute_api.service.AuditService;
import com.nt.red_distribute_api.service.PermissionMenuService;

import jakarta.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/system_admin")
public class SystemAdminController {
    @Autowired
    private PermissionMenuService permissionMenuService;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private AuditService auditService;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @GetMapping("/permission")
    public ResponseEntity<DefaultControllerResp> getSaMenuPermission(
        HttpServletRequest request,
        @RequestParam(name = "permission_id")Long permissionID
    ){
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            PermissionMenuEntity sapm = permissionMenuService.getMenuPermission(permissionID);
        
            if (sapm != null) {
                resp.setRecordsFiltered(1);
                resp.setRecordsTotal(1);
                resp.setCount(1);
                resp.setData(sapm);
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

    @GetMapping("/permissions")
    public ResponseEntity<DefaultControllerResp> getAllSaMenuPermission(
        HttpServletRequest request,
        @RequestParam(name = "draw", defaultValue = "11")Integer draw,
        @RequestParam(name = "order[0][dir]", defaultValue = "ASC")String sortBy,
        @RequestParam(name = "order[0][name]", defaultValue = "created_date")String sortName,
        @RequestParam(name = "start", defaultValue = "0")Integer start,
        @RequestParam(name = "length", defaultValue = "10")Integer length,
        @RequestParam(name = "Search", defaultValue = "")String search,
        @RequestParam(name = "Search_field", defaultValue = "")String searchField
    ){
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        
        DefaultControllerResp resp = new DefaultControllerResp();
        System.out.println("sortBy:" + sortBy);
        try {
            PermissionListReq req = new PermissionListReq(draw, sortBy, sortName, start, length, search, searchField);
            PaginationDataResp listSaMnPm = permissionMenuService.ListMenuPermission(req);

            ObjectMapper objectMapper = new ObjectMapper();
            String respStr = objectMapper.writeValueAsString(listSaMnPm);
            
            resp.setRecordsFiltered(listSaMnPm.getCount());
            resp.setRecordsTotal(listSaMnPm.getCount());
            resp.setCount(listSaMnPm.getCount());
            resp.setData(listSaMnPm.getData());
            resp.setDraw(req.getDraw());
            resp.setStatusCode(HttpStatus.OK.value());
            resp.setMessage("Successfully");

            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setCount(0);
            resp.setDraw(draw);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while getting : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/permission")
    public ResponseEntity<DefaultControllerResp> addSaMenuPermission(HttpServletRequest request, @RequestBody AddPermissionReq  addPermissionReq){
        DefaultControllerResp resp = new DefaultControllerResp();
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        
        try {
            Long createdlastID = permissionMenuService.addSaMenuPermission(addPermissionReq, vsf.getUsername());
            
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("create");
            auditLog.setAuditable_id(createdlastID+1);
            auditLog.setAuditable("sa_menu_permission");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setIp_address(ipAddress);
            auditLog.setComment("addSaMenuPermission");
            auditLog.setCreated_date(DateTime.getTimeStampNow());
            auditService.AddAuditLog(auditLog);

            LogFlie.logMessage(
                "SystemAdminController", 
                String.format("audit_logs/%s/add",LogFlie.dateFolderName()),
                String.format(
                    "%s %s %s %s %s %s %s %s %s",
                    df.format(new Date()),
                    "insert",
                    "addSaMenuPermission",
                    "sa_menu_permission",
                    vsf.getUsername(),
                    ipAddress,
                    vsf.getDevice(),
                    vsf.getBrowser(),
                    vsf.getSystem()
                )
            );
            
            resp.setCount(1);
            resp.setData(addPermissionReq);
            resp.setStatusCode(HttpStatus.OK.value());
            resp.setMessage("Successfully added");

            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setCount(0);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setData(null);
            resp.setMessage("Error while adding : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/permission")
    public ResponseEntity<DefaultControllerResp> updateSaMenuPermission(HttpServletRequest request, @RequestBody UpdateByPermissionReq  updateReq){
        DefaultControllerResp resp = new DefaultControllerResp();
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        
        try {
            permissionMenuService.updatePermission(updateReq.getUpdateID(), updateReq.getUpdateInfo(), vsf.getUsername());
            
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("update");
            auditLog.setAuditable_id(updateReq.getUpdateID());
            auditLog.setAuditable("sa_menu_permission");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setIp_address(ipAddress);
            auditLog.setComment("updateSaMenuPermission");
            auditLog.setCreated_date(DateTime.getTimeStampNow());
            auditService.AddAuditLog(auditLog);

            LogFlie.logMessage(
                "SystemAdminController", 
                String.format("audit_logs/%s/update",LogFlie.dateFolderName()),
                String.format(
                    "%s %s %s %s %s %s %s %s %s",
                    df.format(new Date()),
                    "update",
                    "updateSaMenuPermission",
                    "sa_menu_permission",
                    vsf.getUsername(),
                    ipAddress,
                    vsf.getDevice(),
                    vsf.getBrowser(),
                    vsf.getSystem()
                )
            );
            
            resp.setCount(1);
            resp.setData(updateReq);
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

    @DeleteMapping("/permission")
    public ResponseEntity<DefaultControllerResp> DeleteSaMenuPermission(HttpServletRequest request, @RequestParam(name = "permission_id") Long permissionID){
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            permissionMenuService.removePermission(permissionID);
            
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("delete");
            auditLog.setAuditable_id(permissionID);
            auditLog.setAuditable("sa_menu_permission");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setIp_address(ipAddress);
            auditLog.setComment("DeleteSaMenuPermission");
            auditLog.setCreated_date(DateTime.getTimeStampNow());
            auditService.AddAuditLog(auditLog);

            LogFlie.logMessage(
                "SystemAdminController", 
                String.format("audit_logs/%s/delete",LogFlie.dateFolderName()),
                String.format(
                    "%s %s %s %s %s %s %s %s %s",
                    df.format(new Date()),
                    "delete",
                    "DeleteSaMenuPermission",
                    "sa_menu_permission",
                    vsf.getUsername(),
                    ipAddress,
                    vsf.getDevice(),
                    vsf.getBrowser(),
                    vsf.getSystem()
                )
            );
            
            resp.setCount(1);
            resp.setData(permissionID);
            resp.setStatusCode(HttpStatus.OK.value());
            resp.setMessage("Successfully deleted");

            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while deleting : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
