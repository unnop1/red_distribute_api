package com.nt.red_distribute_api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.red_distribute_api.enitiy.AuditEntity;
import com.nt.red_distribute_api.service.AuditService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/audits")
public class AuditController {
    @Autowired
    private AuditService auditService;
    @GetMapping
    public ResponseEntity<List<AuditEntity>> getAllUser(Integer page, Integer limit){
        return new ResponseEntity<>( auditService.getAllAudit(page, limit), HttpStatus.OK);
    }
}
