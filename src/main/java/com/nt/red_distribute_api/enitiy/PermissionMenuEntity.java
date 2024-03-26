package com.nt.red_distribute_api.enitiy;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

import jakarta.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table (name = "sa_menu_permission")
public class PermissionMenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "permissionName", unique = false,nullable = false)
    private String permissionName;

    @Column(name = "permission_json", unique = false,nullable = false)
    private String permission_json;

    @Column(name = "createdDate", unique = false,nullable = false)
    private Timestamp createdDate;

    @Column(name = "updatedDate", unique = false,nullable = false)
    private Timestamp updatedDate;
}
