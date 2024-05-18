package com.nt.red_distribute_api.controllers;

import com.nt.red_distribute_api.Auth.JwtHelper;
import com.nt.red_distribute_api.dto.req.notification.AddNotification;
import com.nt.red_distribute_api.dto.resp.DefaultControllerResp;
import com.nt.red_distribute_api.dto.resp.VerifyAuthResp;
import com.nt.red_distribute_api.service.NotificationMessageService;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private JwtHelper helper;

    @Autowired
    private NotificationMessageService notificationMessageService;
    
    @PostMapping("/create")
    public ResponseEntity<DefaultControllerResp> createManageMetric(HttpServletRequest request, @RequestBody AddNotification req) {
        String requestHeader = request.getHeader("Authorization");
        String ipAddress = request.getRemoteAddr();
        VerifyAuthResp vsf = helper.verifyToken(requestHeader);
        DefaultControllerResp resp = new DefaultControllerResp();
        try {
            notificationMessageService.CreateNotificationMessages(req);
            resp.setCount(1);
            resp.setData(req);
            return new ResponseEntity<>(resp, HttpStatus.CREATED);
        } catch (Exception e) {
            resp.setCount(0);
            resp.setData(null);
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setMessage("Error while creating : " + e.getMessage());
            return new ResponseEntity<>( resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
