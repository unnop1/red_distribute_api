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
@Table (name = "sa_log_login")
public class LogLoginEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sa_log_login_seq")
    @SequenceGenerator(name = "sa_log_login_seq", allocationSize = 1)
    @Column(name = "log_id")
    private Long log_id;

    @Column(name = "username", unique = false,nullable = true)
    private String username;

    @Column(name = "password", unique = false,nullable = true)
    private String password;

    @Column(name = "ip_address", unique = false,nullable = true)
    private String ip_address;

    @Column(name = "device", unique = false,nullable = true)
    private String device;
    
    @Column(name = "browser", unique = false,nullable = true)
    private String browser;

    @Column(name = "system", unique = false,nullable = true)
    private String system;

    @Column(name = "is_Disable", unique = false,nullable = true)
    private Integer isDisable=0;

    @Column(name = "login_datetime", unique = false,nullable = true)
    private Timestamp login_datetime;

    @Column(name = "is_login", unique = false,nullable = true)
    private Integer is_login=0;

    @Column(name = "create_date", unique = false,nullable = true)
    private Timestamp create_date;

}
