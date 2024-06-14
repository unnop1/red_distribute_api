package com.nt.red_distribute_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;

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
@Table(name = "CONSUMER")
public class ConsumerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consumer_seq")
    @SequenceGenerator(name = "consumer_seq", allocationSize = 1)
    @Column(name = "ID")
    private Long ID;

    @Column(name = "USERNAME", nullable = true)
    private String username=null;

    @Column(name = "CONSUMER_GROUP", nullable = true)
    private String consumer_group=null;

    @Column(name = "SYSTEM_NAME", nullable = true)
    private String system_name=null;

    @Column(name = "PASSWORD", nullable = true)
    private String password=null;

    @Column(name = "DEPARTMENTNAME", nullable = true)
    private String departmentName=null;

    @Column(name = "CONTRACTNAME", nullable = true)
    private String contactName=null;

    @Column(name = "EMAIL", nullable = true)
    private String email=null;

    @Column(name = "PHONENUMBER", nullable = true)
    private String phoneNumber=null;

    @Column(name = "IS_ENABLE", nullable = true)
    private Integer is_enable=1;

    @Column(name = "IS_DELETE", nullable = true)
    private Integer is_delete=0;
    
    @Column(name = "IS_DELETE_DATE", nullable = true)
    private Timestamp is_delete_date=null;

    @Column(name = "IS_DELETE_BY", nullable = true)
    private String is_delete_by=null;

    @Column(name = "CREATED_DATE", nullable = true)
    private Timestamp created_date=null;

    @Column(name = "CREATED_BY", nullable = true)
    private String created_by=null;

    @Column(name = "UPDATED_DATE", nullable = true)
    private Timestamp updated_date=null;

    @Column(name = "UPDATE_BY", nullable = true)
    private String updated_by=null;
}
