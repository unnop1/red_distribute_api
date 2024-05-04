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
public class OrderTypeDashboardTrigger {
        
        @Id
        @Column(name = "ID")
        private Long ID;

        @Column(name = "SA_CHANNEL_CONNECT", unique = false,nullable = true)
        private Long SA_CHANNEL_CONNECT = null;

        @Column(name = "OrderType_Name", unique = false,nullable = true)
        private String OrderTypeName = null;

        @Column(name = "DESCRIPTION", unique = false,nullable = true)
        private String DESCRIPTION = null;

        @Column(name = "MESSAGE_EXPIRE", unique = false,nullable = true)
        private String MESSAGE_EXPIRE = null;

        @Column(name = "Is_Enable", unique = false,nullable = true)
        private Integer IsEnable = 1;

        @Column(name = "Is_Delete", unique = false,nullable = true)
        private Integer IsDelete = 0;

        @Column(name = "TotalConsumer", unique = false,nullable = true)
        private Integer TotalConsumer = 0;

        @Column(name = "TotalTrigger", unique = false,nullable = true)
        private Integer TotalTrigger = 0;

        @Column(name = "CHANNEL_NAME", unique = false,nullable = true)
        private String CHANNEL_NAME;
}
