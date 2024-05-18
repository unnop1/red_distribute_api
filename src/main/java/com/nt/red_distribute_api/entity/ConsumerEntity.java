package com.nt.red_distribute_api.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

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
@Table(name = "consumer")
public class ConsumerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consumer_seq")
    @SequenceGenerator(name = "consumer_seq", allocationSize = 1)
    @Column(name = "ID")
    private Long ID;

    @Column(name = "username", nullable = true)
    private String username=null;

    @Column(name = "consumer_group", nullable = true)
    private String consumer_group=null;

    @Column(name = "system_name", nullable = true)
    private String system_name=null;

    @Column(name = "password", nullable = true)
    private String password=null;

    @Column(name = "departmentName", nullable = true)
    private String departmentName=null;

    @Column(name = "contactName", nullable = true)
    private String contactName=null;

    @Column(name = "email", nullable = true)
    private String email=null;

    @Column(name = "phonenumber", nullable = true)
    private String phoneNumber=null;

    @Column(name = "is_enable", nullable = true)
    private Integer is_enable=1;

    @Column(name = "is_delete", nullable = true)
    private Integer is_delete=0;
    
    @Column(name = "is_delete_date", nullable = true)
    private Timestamp is_delete_date=null;

    @Column(name = "is_delete_by", nullable = true)
    private String is_delete_by=null;

    @Column(name = "created_date", nullable = true)
    private Timestamp created_date=null;

    @Column(name = "created_by", nullable = true)
    private String created_by=null;

    @Column(name = "updated_date", nullable = true)
    private Timestamp updated_date=null;

    @Column(name = "updated_by", nullable = true)
    private String updated_by=null;
}
