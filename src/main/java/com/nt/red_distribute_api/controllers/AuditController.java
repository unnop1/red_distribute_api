package com.nt.red_distribute_api.controllers;

import org.springframework.web.bind.annotation.*;

import com.nt.red_distribute_api.dto.req.audit.ListAuditReq;
import com.nt.red_distribute_api.dto.resp.DefaultControllerResp;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.service.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/audit")
public class AuditController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuditService auditService;


    @GetMapping("list")
    public ResponseEntity<DefaultControllerResp> ListAudit(
        @RequestParam(name = "draw", defaultValue = "11")Integer draw,
        @RequestParam(name = "order[0][dir]", defaultValue = "ASC")String sortBy,
        @RequestParam(name = "order[0][name]", defaultValue = "id")String sortName,
        @RequestParam(name = "start_time", defaultValue = "0")String startTime,
        @RequestParam(name = "end_time", defaultValue = "0")String endTime,
        @RequestParam(name = "start", defaultValue = "0")Integer start,
        @RequestParam(name = "length", defaultValue = "0")Integer length,
        @RequestParam(name = "Search", defaultValue = "")String search,
        @RequestParam(name = "Search_field", defaultValue = "")String searchField
    ) {
        System.out.println("draw: " + draw);    
        DefaultControllerResp response = new DefaultControllerResp();
        // try {
            ListAuditReq req = new ListAuditReq(draw, sortBy, sortName,  start, length, search, searchField);
            PaginationDataResp listAudits = auditService.ListAllAudit(req);
            response.setRecordsFiltered(listAudits.getCount());
            response.setRecordsTotal(listAudits.getCount());
            response.setCount(listAudits.getCount());
            response.setMessage("Success");
            response.setData(listAudits.getData());
            response.setStatusCode(200);
            return new ResponseEntity<>(response, HttpStatus.OK);
        // } catch (Exception e) {
        //     response.setCount(0);
        //     response.setData(null);
        //     response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        //     response.setMessage("Error while getting : " + e.getMessage());
        //     return new ResponseEntity<>( response, HttpStatus.INTERNAL_SERVER_ERROR);
        // }
    }
    
}