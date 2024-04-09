package com.nt.red_distribute_api.controllers;

import com.nt.red_distribute_api.Auth.JwtHelper;
import com.nt.red_distribute_api.config.AuthConfig;
import com.nt.red_distribute_api.dto.req.JwtRequest;
import com.nt.red_distribute_api.dto.req.UserRequestDto;
import com.nt.red_distribute_api.dto.resp.AuthSuccessResp;
import com.nt.red_distribute_api.dto.resp.JwtErrorResp;
import com.nt.red_distribute_api.dto.resp.LoginResp;
import com.nt.red_distribute_api.dto.resp.UserResp;
import com.nt.red_distribute_api.enitiy.LogLoginEntity;
import com.nt.red_distribute_api.enitiy.PermissionMenuEntity;
import com.nt.red_distribute_api.enitiy.UserEnitiy;
import com.nt.red_distribute_api.exp.UserAlreadyExistsException;
import com.nt.red_distribute_api.service.LogLoginService;
import com.nt.red_distribute_api.service.PermissionMenuService;
import com.nt.red_distribute_api.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

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
    private UserService userService;

    @Autowired
    private LogLoginService logloginService;

    @Autowired
    private PermissionMenuService permissionMenuService; 


    @PostMapping("/create")
    public ResponseEntity<AuthSuccessResp> createUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            UserResp userResponseDto = userService.createUser(userRequestDto);
            UserDetails userDetails = userService.loadUserByUsername(userResponseDto.getEmail());
            System.out.println("from db info");
            System.out.println(userDetails.getUsername());
            System.out.println(userDetails.getPassword());

            String token = this.helper.generateToken(userDetails);
            return new ResponseEntity<>(new AuthSuccessResp(token), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException ex) {
            // Handle the exception and return an appropriate response
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new AuthSuccessResp("User already exists: " + ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResp> login(@RequestBody JwtRequest jwtRequest, HttpServletRequest request) {
    // Get the IP address from the request
    String ipAddress = request.getRemoteAddr();
    // String userAgent = request.getHeader("User-Agent");
    // String deviceInfo = parseUserAgent(userAgent);
    // String systemInfo = parseUserAgentForSystem(userAgent);
    // String browserInfo = parseUserAgentForBrowser(userAgent);
    System.out.println("IP Address: " + ipAddress);
  
    // Log login
    Timestamp loginDateTime = new Timestamp(Instant.now().toEpochMilli());
    this.doAuthenticate(jwtRequest, ipAddress, loginDateTime);
    UserEnitiy userDetails = userService.loadUserByUsername(jwtRequest.getEmail());

    String token = this.helper.generateToken(userDetails);

    HashMap<String, Object> updateInfo = new HashMap<String, Object>();
    updateInfo.put("currentToken", token);
    updateInfo.put("last_login", loginDateTime);
    updateInfo.put("last_login_ipaddress", ipAddress);

    this.userService.updateUser(userDetails.getUsername(), updateInfo);

    LoginResp userResp = new LoginResp();
    UserResp userInfo = new UserResp();
    // PermissionMenu permissionMenu = 

    // User
    userInfo.setId(userDetails.getId());
    userInfo.setAboutMe(userDetails.getAboutMe());
    userInfo.setName(userDetails.getName());
    userInfo.setPhoneNumber(userDetails.getPhoneNumber());
    userInfo.setEmail(userDetails.getUsername());
    userInfo.setLast_login(userDetails.getLast_login());
    userInfo.setLast_login_ipaddress(ipAddress);
    userInfo.setCreated_by(userDetails.getCreated_by());
    userInfo.setCreatedDate(userDetails.getCreatedDate());
    userInfo.setIsDelete_by(userDetails.getIsDelete_by());
    userInfo.setIsDelete(userDetails.getIsDelete());
    userInfo.setUpdatedDate(userDetails.getUpdatedDate());
    userInfo.setUpdated_by(userDetails.getUpdated_by());
    userResp.setUserLogin(userInfo);
    userResp.setJwtToken(token);

    // permissionMenu
    PermissionMenuEntity permissionMenuEntity = permissionMenuService.getUserMenuPermission(userDetails.getId());
    userResp.setPermissionJson(permissionMenuEntity.getPermission_json());
    userResp.setPermissionName(permissionMenuEntity.getPermissionName());

    System.out.println("token:"+token);

    return new ResponseEntity<>(userResp, HttpStatus.OK);
}

    private void doAuthenticate(JwtRequest jwtRequest, String ipAddress, Timestamp loginDateTime) {
        
        System.out.println("Login Info");
        String email = jwtRequest.getEmail();
        String password = jwtRequest.getPassword();
        System.out.println(email);
        System.out.println(password);
        System.out.println("------");
        LogLoginEntity loglogin = new LogLoginEntity();
        loglogin.setBrowser(jwtRequest.getBrowser());
        loglogin.setDevice(jwtRequest.getDevice());
        loglogin.setSystem(jwtRequest.getSystem());
        loglogin.setIp_address(ipAddress);
        loglogin.setLogin_datetime(loginDateTime);
        loglogin.setCreate_date(loginDateTime);
        loglogin.setUsername(jwtRequest.getUsername());
        try {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
            manager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Authentication successful for user: " + email);
            this.logloginService.createLog(loglogin);

        } catch (Exception e) {
            System.out.println("Authentication not-successful for user: " + email);
            loglogin.setPassword(password);
            this.logloginService.createLog(loglogin);
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<JwtErrorResp> exceptionHandler(BadCredentialsException ex) {
        return new ResponseEntity<>(new JwtErrorResp(400, "Credentials Invalid !!"), HttpStatus.BAD_REQUEST);
    }
}
