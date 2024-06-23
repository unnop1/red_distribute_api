package com.nt.red_distribute_api.controllers;

import com.nt.red_distribute_api.Auth.JwtHelper;
import com.nt.red_distribute_api.dto.req.audit.AuditLog;
import com.nt.red_distribute_api.dto.req.user.ListUserReq;
import com.nt.red_distribute_api.dto.req.user.UpdateUserDto;
import com.nt.red_distribute_api.dto.resp.DefaultControllerResp;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.dto.resp.UserInfoResp;
import com.nt.red_distribute_api.dto.resp.UserResp;
import com.nt.red_distribute_api.dto.resp.VerifyAuthResp;
import com.nt.red_distribute_api.entity.PermissionMenuEntity;
import com.nt.red_distribute_api.service.AuditService;
import com.nt.red_distribute_api.service.PermissionMenuService;
import com.nt.red_distribute_api.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private PermissionMenuService permissionMenuService;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private AuditService auditService;

    @GetMapping("/list")
    public ResponseEntity<DefaultControllerResp> getAllUser(
        HttpServletRequest request,
        @RequestParam(name = "draw", defaultValue = "11")Integer draw,
        @RequestParam(name = "order[0][dir]", defaultValue = "ASC")String sortBy,
        @RequestParam(name = "order[0][name]", defaultValue = "created_date")String sortName,
        @RequestParam(name = "start", defaultValue = "0")Integer start,
        @RequestParam(name = "length", defaultValue = "10")Integer length,
        @RequestParam(name = "Search", defaultValue = "")String search,
        @RequestParam(name = "Search_field", defaultValue = "")String search_field
    ){
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
       
        DefaultControllerResp response = new DefaultControllerResp();
        try{
            ListUserReq req = new ListUserReq(draw, sortBy, sortName, start, length, search, search_field);
            PaginationDataResp smsGateways = userService.getAllUser(req);
            
            response.setRecordsFiltered(smsGateways.getCount());
            response.setRecordsTotal(smsGateways.getCount());
            response.setCount(smsGateways.getCount());
            response.setMessage("Success");
            response.setDraw(req.getDraw());
            response.setData(smsGateways.getData());
            response.setStatusCode(200);

            return new ResponseEntity<>( response, HttpStatus.OK);
        }catch (Exception e){
            response.setDraw(draw);
            response.setCount(0);
            response.setData(null);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error while getting : " + e.getMessage());
            return new ResponseEntity<>( response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResp> getUserInfo(
        HttpServletRequest request,
        @RequestParam(name = "user_id")Long userId
    ){
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
       
        UserInfoResp userResp = new UserInfoResp();
        try{
            UserResp userInfo = userService.findUserById(userId);

            if (userInfo != null){
                PermissionMenuEntity permissionMenuEntity = permissionMenuService.getMenuPermission(userInfo.getSa_menu_permission_id());
                userResp.setUserLogin(userInfo);
                userResp.setPermissionJson(permissionMenuEntity.getPermission_json());
                userResp.setPermissionName(permissionMenuEntity.getPermission_Name());
                return new ResponseEntity<>( userResp, HttpStatus.OK);
            }else{
                return new ResponseEntity<>( userResp, HttpStatus.NOT_FOUND);
            }

            
        }catch (Exception e){
            userResp.setPermissionName(e.getMessage());
            return new ResponseEntity<>( userResp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<DefaultControllerResp> updateUser(HttpServletRequest request, @RequestBody UpdateUserDto req) throws Exception{
        
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);

        DefaultControllerResp response = new DefaultControllerResp();
        try{
            userService.updateUser(req, vsf.getUsername());
            
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("update");
            auditLog.setAuditable_id(req.getId());
            auditLog.setAuditable("user_db");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setIp_address(ipAddress);
            auditLog.setComment("updateUser");
            auditService.AddAuditLog(auditLog);

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

    @DeleteMapping("by_id")
    public ResponseEntity<DefaultControllerResp> DeleteUser(HttpServletRequest request, @RequestParam(name = "user_id") Long userID){
        String ipAddress = request.getRemoteAddr();
        String requestHeader = request.getHeader("Authorization");
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
       
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            userService.removeUser(userID);

            AuditLog auditLog = new AuditLog();
            auditLog.setAction("delete");
            auditLog.setAuditable_id(userID);
            auditLog.setAuditable("user_db");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setIp_address(ipAddress);
            auditLog.setComment("DeleteUser");
            auditService.AddAuditLog(auditLog);

            resp.setCount(1);
            resp.setData(userID);
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
