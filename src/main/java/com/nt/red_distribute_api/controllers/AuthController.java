package com.nt.red_distribute_api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.nt.red_distribute_api.Auth.JwtHelper;
import com.nt.red_distribute_api.Util.Convert;
import com.nt.red_distribute_api.Util.DateTime;
import com.nt.red_distribute_api.dto.req.audit.AuditLog;
import com.nt.red_distribute_api.dto.req.auth.JwtRequest;
import com.nt.red_distribute_api.dto.req.user.UserRequestDto;
import com.nt.red_distribute_api.dto.resp.AuthSuccessResp;
import com.nt.red_distribute_api.dto.resp.JwtErrorResp;
import com.nt.red_distribute_api.dto.resp.LoginResp;
import com.nt.red_distribute_api.dto.resp.UserResp;
import com.nt.red_distribute_api.dto.resp.VerifyAuthResp;
import com.nt.red_distribute_api.entity.LogLoginEntity;
import com.nt.red_distribute_api.entity.PermissionMenuEntity;
import com.nt.red_distribute_api.entity.UserEntity;
import com.nt.red_distribute_api.exp.UserAlreadyExistsException;
import com.nt.red_distribute_api.service.AuditService;
import com.nt.red_distribute_api.service.LogLoginService;
import com.nt.red_distribute_api.service.PermissionMenuService;
import com.nt.red_distribute_api.service.UserService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.function.ServerResponse.BodyBuilder;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserService userService;

    @Autowired
    private LogLoginService logloginService;

    @Autowired
    private PermissionMenuService permissionMenuService; 

    private final ObjectMapper objectMapper = new ObjectMapper();


    @PostMapping("/create")
    public ResponseEntity<AuthSuccessResp> createUser(HttpServletRequest request, @RequestBody UserRequestDto userRequestDto) {
        String requestHeader = request.getHeader("Authorization");
        String ipAddress = request.getRemoteAddr();
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);
        try {
            UserResp userResponseDto = userService.createUser(userRequestDto, vsf.getUsername());
            UserEntity userDetails = userService.loadUserByUsername(userResponseDto.getUsername());
            
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("create");
            auditLog.setAuditable("user_db");
            auditLog.setUsername(vsf.getUsername());
            auditLog.setDevice(vsf.getDevice());
            auditLog.setOperating_system(vsf.getSystem());
            auditLog.setBrowser(vsf.getBrowser());
            auditLog.setIp_address(ipAddress);
            auditLog.setAuditable_id(userDetails.getId());
            auditLog.setComment("createUser");
            auditLog.setCreated_date(DateTime.getTimeStampNow());
            auditService.AddAuditLog(auditLog);

            JwtRequest jwtReq = new JwtRequest();
            jwtReq.setUsername(vsf.getUsername());
            jwtReq.setBrowser(vsf.getBrowser());
            jwtReq.setDevice(vsf.getDevice());
            jwtReq.setSystem(vsf.getSystem());

            String token = this.helper.generateToken(jwtReq, userDetails.getEmail());
            return new ResponseEntity<>(new AuthSuccessResp(token), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException ex) {
            // Handle the exception and return an appropriate response
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new AuthSuccessResp("User already exists: " + ex.getMessage()));
        }
    }

    @PostMapping(value="/login", produces = "application/json")
    @ResponseBody
    public LoginResp login(@RequestBody JwtRequest jwtRequest, HttpServletRequest request) throws java.io.IOException {
        // Get the IP address from the request
        String ipAddress = request.getRemoteAddr();
        logger.info("IP Address: {}", ipAddress);
        logger.info("jwtUsername: {}", jwtRequest.getUsername());

        // Log login
        LoginResp loginResp = new LoginResp();
        Timestamp loginDateTime = new Timestamp(Instant.now().toEpochMilli());
        LogLoginEntity loglogin = new LogLoginEntity();
        loglogin.setBrowser(jwtRequest.getBrowser());
        loglogin.setDevice(jwtRequest.getDevice());
        loglogin.setSystem(jwtRequest.getSystem());
        loglogin.setIp_address(ipAddress);
        loglogin.setLogin_datetime(loginDateTime);
        loglogin.setCreate_date(loginDateTime);
        loglogin.setUsername(jwtRequest.getUsername());

        UserEntity userDetails = userService.findUserLogin(jwtRequest.getUsername());
        if (userDetails == null) {
            logger.error("User not found: {}", jwtRequest.getUsername());
            // return ResponseEntity.badRequest().body(null);
            return loginResp;
        }

        this.doAuthenticate(userDetails.getUsername(), jwtRequest.getPassword(), loglogin);

        String token = this.helper.generateToken(jwtRequest, userDetails.getEmail());
        logger.info("Generated token: {}", token);

        HashMap<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("currentToken", token);
        updateInfo.put("last_login", loginDateTime);
        updateInfo.put("last_login_ipaddress", ipAddress);
        this.userService.updateUserLogLogin(userDetails.getId(), updateInfo);

        UserResp userInfo = new UserResp();
        userInfo.setId(userDetails.getId());
        userInfo.setAbout_Me(userDetails.getAbout_me());
        userInfo.setName(userDetails.getName());
        userInfo.setUsername(userDetails.getUsername());
        userInfo.setPhoneNumber(userDetails.getPhoneNumber());
        userInfo.setEmail(userDetails.getEmail());
        userInfo.setLast_login(userDetails.getLast_login());
        userInfo.setLast_login_ipaddress(ipAddress);
        userInfo.setCreated_by(userDetails.getCreated_by());
        userInfo.setCreated_Date(userDetails.getCreated_Date());
        userInfo.setIs_Enable(userDetails.getIs_Enable());
        userInfo.setIs_Delete_by(userDetails.getIs_Delete_by());
        userInfo.setIs_Delete(userDetails.getIs_Delete());
        userInfo.setUpdated_Date(userDetails.getUpdated_Date());
        userInfo.setUpdated_by(userDetails.getUpdated_by());

        
        loginResp.setUserLogin(userInfo);
        loginResp.setJwtToken(token);

        PermissionMenuEntity permissionMenuEntity = permissionMenuService.getMenuPermission(userDetails.getSa_menu_permission_id());
        String permissionJSonStr;
        try {
            permissionJSonStr = Convert.clobToString(permissionMenuEntity.getPermission_json());
            loginResp.setPermissionJson(permissionJSonStr);
        } catch (java.io.IOException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        loginResp.setPermissionName(permissionMenuEntity.getPermission_Name());

        AuditLog auditLog = new AuditLog();
        auditLog.setAction("login");
        auditLog.setAuditable_id(userDetails.getId());
        auditLog.setAuditable("user_db");
        auditLog.setIp_address(ipAddress);
        auditLog.setUsername(userDetails.getUsername());
        auditLog.setDevice(jwtRequest.getDevice());
        auditLog.setBrowser(jwtRequest.getBrowser());
        auditLog.setOperating_system(jwtRequest.getSystem());
        auditLog.setComment("authentication login");
        auditLog.setCreated_date(DateTime.getTimeStampNow());
        auditService.AddAuditLog(auditLog);

        logger.info("Response user info: {}", loginResp.getUserLogin());
        logger.info("Response JWT token: {}", loginResp.getJwtToken());
        logger.info("Response permission JSON: {}", loginResp.getPermissionJson());
        logger.info("Response permission name: {}", loginResp.getPermissionName());

        // return ResponseEntity.ok(loginResp);
        if(loginResp.getJwtToken()==null){
            loginResp.setJwtToken("error jwt");
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResp);
            return loginResp;
        }
        // return new ResponseEntity<>(loginResp, HttpStatus.OK);
        // return ResponseEntity.status(HttpStatus.OK).body(loginResp);
        return loginResp;

    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResp> refresh(HttpServletRequest request) {
        // Get the IP address from the request
        LoginResp userResp = new LoginResp();
        System.out.println("request:"+request.getHeaderNames());
        String requestHeader = request.getHeader("Authorization");
        String token = requestHeader.substring(7);
            
        VerifyAuthResp vsf = this.helper.verifyToken(requestHeader);
        if (vsf.getError() == null) {
            UserEntity userDetails = vsf.getUserInfo();
            if( userDetails == null ){
                return new ResponseEntity<>(userResp, HttpStatus.BAD_REQUEST);
            }
            UserResp userInfo = new UserResp();
            // PermissionMenu permissionMenu = 

            // User
            userInfo.setId(userDetails.getId());
            userInfo.setAbout_Me(userDetails.getAbout_me());
            userInfo.setName(userDetails.getName());
            userInfo.setPhoneNumber(userDetails.getPhoneNumber());
            userInfo.setIs_Enable(userDetails.getIs_Enable());
            userInfo.setEmail(userDetails.getEmail());
            userInfo.setLast_login(userDetails.getLast_login());
            userInfo.setCreated_by(userDetails.getCreated_by());
            userInfo.setCreated_Date(userDetails.getCreated_Date());
            userInfo.setIs_Delete_by(userDetails.getIs_Delete_by());
            userInfo.setIs_Delete(userDetails.getIs_Delete());
            userInfo.setUpdated_Date(userDetails.getUpdated_Date());
            userInfo.setSa_menu_permission_id(userDetails.getSa_menu_permission_id());
            userInfo.setUpdated_by(userDetails.getUpdated_by());
            userResp.setUserLogin(userInfo);
            userResp.setJwtToken(token);

            // permissionMenu
            PermissionMenuEntity permissionMenuEntity = permissionMenuService.getMenuPermission(userDetails.getSa_menu_permission_id());
            String permissionJSonStr;
            try {
                permissionJSonStr = Convert.clobToString(permissionMenuEntity.getPermission_json());
                userResp.setPermissionJson(permissionJSonStr);
            } catch (java.io.IOException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            userResp.setPermissionName(permissionMenuEntity.getPermission_Name());

            return new ResponseEntity<>(userResp, HttpStatus.OK);
        }
        

        return new ResponseEntity<>(userResp, HttpStatus.BAD_REQUEST);
    }

    private void doAuthenticate(String username, String password, LogLoginEntity loglogin) {
        System.out.println("Login Info");
        System.out.println(username);
        System.out.println(password);
        System.out.println("------");
        try {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
            manager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Authentication successful for user: " + username);
            loglogin.setIs_login(1);
            this.logloginService.createLog(loglogin);

        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
            System.out.println("Authentication not-successful for user: " + username);
            loglogin.setPassword(password);
            loglogin.setIs_login(0);
            System.out.println(loglogin.toString());
            this.logloginService.createLog(loglogin);
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<JwtErrorResp> exceptionHandler(BadCredentialsException ex) {
        return new ResponseEntity<>(new JwtErrorResp(400, "Credentials Invalid !!"), HttpStatus.BAD_REQUEST);
    }
}
