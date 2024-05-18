package com.nt.red_distribute_api.entity;



import java.sql.Timestamp;

import javax.persistence.*;
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
@Table(name = "AUDIT_LOG")
public class AuditLogEntity {
 
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_log_seq")
        @SequenceGenerator(name = "audit_log_seq", allocationSize = 1)
        @Column(name = "AUDIT_ID")
        private Long AUDIT_ID;

        @Column(name = "Action", nullable = true)
        private String action;

        @Column(name = "AUDITABLE_ID", nullable = true)
        private Long auditable_id;

        @Column(name = "AUDITABLE", nullable = true)
        private String auditable;

        @Column(name = "Comments", nullable = true)
        private String comment;

        @Column(name = "IP_ADDRESS", nullable = true)
        private String ip_address;

        @Column(name = "USERNAME", nullable = true)
        private String username;

        @Column(name = "DEVICE", nullable = true)
        private String device;

        @Column(name = "BROWSER", nullable = true)
        private String browser;

        @Column(name = "Operating_system", nullable = true) // Renamed to avoid reserved keyword
        private String operating_system;

        @Column(name = "CREATED_DATE", nullable = true)
        private Timestamp created_date;
}