package com.nt.red_distribute_api.dto.resp;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderTypeResponseDto {
    @Column(name = "TYPEID")
    private Long TYPEID = null;

    @Column(name = "MainID", unique = false,nullable = true)
    private Long MainID = null;

    @Column(name = "OrderType_Name", unique = false,nullable = true)
    private String OrderTypeName = null;

    @Column(name = "Is_Enable", unique = false,nullable = true)
    private Integer IsEnable = 1;

    @Column(name = "Is_Delete", unique = false,nullable = true)
    private Integer IsDelete = 0;

    @Column(name = "Created_Date", unique = false,nullable = true)
    private Timestamp CreatedDate = null;

    @Column(name = "CreatedBy_UserID", unique = false,nullable = true)
    private Long CreatedBy_UserID = null;

    @Column(name = "Updated_Date", unique = false,nullable = true)
    private Timestamp UpdatedDate = null;

    @Column(name = "UpdatedBy_UserID", unique = false,nullable = true)
    private Long UpdatedBy_UserID = null;


}
