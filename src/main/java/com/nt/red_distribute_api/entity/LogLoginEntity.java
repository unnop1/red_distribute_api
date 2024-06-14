package com.nt.red_distribute_api.entity;

import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@Setter
@ToString
@Table (name = "SA_LOG_LOGIN")
public class LogLoginEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sa_log_login_seq")
    @SequenceGenerator(name = "sa_log_login_seq", allocationSize = 1)
    @Column(name = "LOG_ID")
    private Long log_id;

    @Column(name = "USERNAME", unique = false,nullable = true)
    private String username;

    @Column(name = "PASSWORD", unique = false,nullable = true)
    private String password;

    @Column(name = "IP_ADDRESS", unique = false,nullable = true)
    private String ip_address;

    @Column(name = "DEVICE", unique = false,nullable = true)
    private String device;
    
    @Column(name = "BROWSER", unique = false,nullable = true)
    private String browser;

    @Column(name = "SYSTEM", unique = false,nullable = true)
    private String system;

    @Column(name = "IS_DISABLE", unique = false,nullable = true)
    private Integer isDisable=0;

    @Column(name = "LOGIN_DATETIME", unique = false,nullable = true)
    private Timestamp login_datetime;

    @Column(name = "IS_LOGIN", unique = false,nullable = true)
    private Integer is_login=0;

    @Column(name = "CREATE_DATE", unique = false,nullable = true)
    private Timestamp create_date;

}
