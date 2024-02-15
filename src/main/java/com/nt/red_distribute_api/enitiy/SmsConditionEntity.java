package com.nt.red_distribute_api.enitiy;


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
@Table (name = "sms_conditions")
public class SmsConditionEntity {
        
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "SMSID")
        private Long SMSID = null;

        @Column(name = "order_type_MainID", unique = false,nullable = true)
        private Long order_type_MainID = null;
        
        @Column(name = "OrderType", unique = false,nullable = true)
        private String OrderType = null;

        @Column(name = "Message", unique = false,nullable = true)
        private String Message = null;

        @Column(name = "Chanel", unique = false,nullable = true)
        private String Chanel = null;

        @Column(name = "Frequency", unique = false,nullable = true)
        private String Frequency = null;

        @Column(name = "serviceType", unique = false,nullable = true)
        private Integer serviceType = null;

        @Column(name = "by_offeringId", unique = false,nullable = true)
        private String by_offeringId = null;

        @Column(name = "DateStart", unique = false,nullable = true)
        private String DateStart = null;

        @Column(name = "DateEnd", unique = false,nullable = true)
        private String DateEnd = null;

        @Column(name = "IsEnable", unique = false,nullable = true)
        private Boolean IsEnable = true;

        @Column(name = "IsDelete", unique = false,nullable = true)
        private Boolean IsDelete = false;

        @Column(name = "CreatedDate", unique = false,nullable = true)
        private String CreatedDate = null;

        @Column(name = "CreatedBy_UserID", unique = false,nullable = true)
        private Long CreatedBy_UserID = null;

        @Column(name = "UpdatedDate", unique = false,nullable = true)
        private String UpdatedDate = null;

        @Column(name = "UpdatedBy_UserID", unique = false,nullable = true)
        private Long UpdatedBy_UserID = null;
}
