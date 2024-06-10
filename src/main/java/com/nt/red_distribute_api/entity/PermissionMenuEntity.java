package com.nt.red_distribute_api.entity;


import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table (name = "sa_menu_permission")
public class PermissionMenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sa_menu_permission_seq")
    @SequenceGenerator(name = "sa_menu_permission_seq", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PERMISSION_NAME", unique = false,nullable = true)
    private String permission_Name=null;

    @Column(name = "PERMISSION_JSON", unique = false,nullable = true)
    private String permission_json=null;

    @Column(name = "CREATED_DATE", unique = false,nullable = true)
    private Timestamp created_Date=null;
    
    @Column(name = "CREATED_BY", unique = false,nullable = true)
    private String created_By=null;

    @Column(name = "UPDATED_DATE", unique = false,nullable = true)
    private Timestamp updated_Date=null;

    @Column(name = "UPDATED_BY", unique = false,nullable = true)
    private String updated_By=null;
}
