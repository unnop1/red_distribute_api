package com.nt.red_distribute_api.enitiy;


import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table (name = "order_type")
public class OrderTypeEntity {
        
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "TYPEID")
        private Long TYPEID = null;

        @Column(name = "MainID", unique = false,nullable = true)
        private Long MainID = null;

        @Column(name = "OrderTypeName", unique = false,nullable = true)
        private String OrderTypeName = null;

        @Column(name = "IsEnable", unique = false,nullable = true)
        private Integer IsEnable = 1;

        @Column(name = "IsDelete", unique = false,nullable = true)
        private Integer IsDelete = 0;

        @Column(name = "CreatedDate", unique = false,nullable = true)
        private Timestamp CreatedDate = null;

        @Column(name = "CreatedBy_UserID", unique = false,nullable = true)
        private Long CreatedBy_UserID = null;

        @Column(name = "UpdatedDate", unique = false,nullable = true)
        private Timestamp UpdatedDate = null;

        @Column(name = "UpdatedBy_UserID", unique = false,nullable = true)
        private Long UpdatedBy_UserID = null;

        @Column(name = "TotleMsg", unique = false,nullable = true)
        private Integer TotleMsg = 0;

        @Column(name = "TotleSend", unique = false,nullable = true)
        private Integer TotleSend = 0;
}
