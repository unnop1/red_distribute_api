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
@Table (name = "sms_gateway")
public class SmsGatewayEntity {
        
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "GID")
        private Long SMSID = null;

        @Column(name = "sms_conditions_SMSID", unique = false,nullable = true)
        private Long sms_conditions_SMSID = null;
        
        @Column(name = "SMSMessage", unique = false,nullable = true)
        private String SMSMessage = null;

        @Column(name = "order_type_MainID", unique = false,nullable = true)
        private Long order_type_MainID = null;

        @Column(name = "OrderType", unique = false,nullable = true)
        private String OrderType = null;

        @Column(name = "PhoneNumber", unique = false,nullable = true)
        private String PhoneNumber = null;

        @Column(name = "serviceType", unique = false,nullable = true)
        private Integer serviceType = null;

        @Column(name = "Frequency", unique = false,nullable = true)
        private String Frequency = null;

        @Column(name = "Chanel", unique = false,nullable = true)
        private String Chanel = null;

        @Column(name = "OfferingId", unique = false,nullable = true)
        private String OfferingId = null;

        @Column(name = "PayloadMQ", unique = false,nullable = true)
        private String PayloadMQ = null;

        @Column(name = "IsStatus", unique = false,nullable = true)
        private Integer IsStatus = 0;

        @Column(name = "CreatedDate", unique = false,nullable = true)
        private Timestamp CreatedDate = null;
}
