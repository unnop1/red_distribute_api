package com.nt.red_distribute_api.controllers;

import com.nt.red_distribute_api.Auth.JwtHelper;
import com.nt.red_distribute_api.Util.DateTime;
import com.nt.red_distribute_api.dto.req.audit.AuditLog;
import com.nt.red_distribute_api.dto.req.permission.AddPermissionReq;
import com.nt.red_distribute_api.dto.req.permission.PermissionListReq;
import com.nt.red_distribute_api.dto.req.permission.UpdateByPermissionReq;
import com.nt.red_distribute_api.dto.resp.DefaultControllerResp;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.dto.resp.VerifyAuthResp;
import com.nt.red_distribute_api.entity.AuditLogEntity;
import com.nt.red_distribute_api.entity.PermissionMenuEntity;
import com.nt.red_distribute_api.service.AuditService;
import com.nt.red_distribute_api.service.PermissionMenuService;

import jakarta.servlet.http.HttpServletRequest;

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
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("get");
            auditLog.setAuditable_id(permissionID);
            auditLog.setAuditable("sa_menu_permission");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setIp_address(ipAddress);
            auditLog.setComment("getSaMenuPermission");
            auditLog.setCreated_date(DateTime.getTimeStampNow());
            auditService.AddAuditLog(auditLog);
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
        try {
            DefaultControllerResp resp = new DefaultControllerResp();
        
            String ipAddress = request.getRemoteAddr();
            String requestHeader = request.getHeader("Authorization");
                
            VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
            
            
            System.out.println("sortBy:" + sortBy);
            
            PermissionListReq req = new PermissionListReq(draw, sortBy, sortName, start, length, search, searchField);
            PaginationDataResp listSaMnPm = permissionMenuService.ListMenuPermission(req);
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("get");
            auditLog.setAuditable("sa_menu_permission");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setIp_address(ipAddress);
            auditLog.setComment("getAllSaMenuPermission");
            auditLog.setCreated_date(DateTime.getTimeStampNow());
            auditService.AddAuditLog(auditLog);
            
            resp.setRecordsFiltered(listSaMnPm.getCount());
            resp.setRecordsTotal(listSaMnPm.getCount());
            resp.setCount(listSaMnPm.getCount());
            resp.setData(listSaMnPm.getData());
            resp.setDraw(req.getDraw());
            resp.setStatusCode(HttpStatus.OK.value());
            resp.setMessage("Successfully");

            return new ResponseEntity<>( resp, HttpStatus.OK);
        }catch (Exception e){
            DefaultControllerResp resperr = new DefaultControllerResp();
            resperr.setCount(0);
            resperr.setData(null);
            resperr.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resperr.setMessage("Error while getting : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resperr);
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
