package com.nt.red_distribute_api.entity.view.order_type;


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
public class ListOrderType {
        
        @Id
        @Column(name = "TYPEID")
        private Long TYPEID = null;

        @Column(name = "MainID", unique = false,nullable = true)
        private Long MainID = null;

        @Column(name = "OrderType_Name", unique = false,nullable = true)
        private String OrderType_Name = null;

        @Column(name = "Is_Enable", unique = false,nullable = true)
        private Integer Is_Enable = 1;

        @Column(name = "Is_Delete", unique = false,nullable = true)
        private Integer Is_Delete = 0;

        @Column(name = "Created_Date", unique = false,nullable = true)
        private Timestamp Created_Date = null;

        @Column(name = "Created_By", unique = false,nullable = true)
        private Long Created_By = null;

        @Column(name = "Updated_Date", unique = false,nullable = true)
        private Timestamp Updated_Date = null;

        @Column(name = "Updated_By", unique = false,nullable = true)
        private Long Updated_By = null;

        @Column(name = "totalmsg", unique = false,nullable = true)
        private Integer totalmsg = 0;

        @Column(name = "totalsend", unique = false,nullable = true)
        private Integer totalsend = 0;

        @Column(name = "totalunsend", unique = false,nullable = true)
        private Integer totalunsend = 0;
}
