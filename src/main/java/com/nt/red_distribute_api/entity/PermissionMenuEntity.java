package com.nt.red_distribute_api.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table (name = "sa_menu_permission")
public class PermissionMenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sa_menu_permission_seq")
    @SequenceGenerator(name = "sa_menu_permission_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "permission_Name", unique = false,nullable = true)
    private String permission_Name=null;

    @Column(name = "permission_json", unique = false,nullable = true)
    private String permission_json=null;

    @Column(name = "created_Date", unique = false,nullable = true)
    private Timestamp created_Date=null;
    
    @Column(name = "created_By", unique = false,nullable = true)
    private String created_By=null;

    @Column(name = "updated_Date", unique = false,nullable = true)
    private Timestamp updated_Date=null;

    @Column(name = "updated_By", unique = false,nullable = true)
    private String updated_By=null;
}
