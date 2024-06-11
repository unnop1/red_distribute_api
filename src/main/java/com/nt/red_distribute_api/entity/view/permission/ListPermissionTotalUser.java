package com.nt.red_distribute_api.entity.view.permission;


import java.sql.Clob;
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
@Table (name = "sa_menu_permission")
public class ListPermissionTotalUser {
        @Id
        @Column(name = "ID")
        private Long id;

        @Column(name = "PERMISSION_NAME", unique = false,nullable = true)
        private String permission_Name=null;

        @Column(name = "PERMISSION_JSON", unique = false,nullable = true)
        private Clob permission_json=null;

        @Column(name = "CREATED_DATE", unique = false,nullable = true)
        private Timestamp created_Date=null;
        
        @Column(name = "CREATED_BY", unique = false,nullable = true)
        private String created_By=null;

        @Column(name = "UPDATED_DATE", unique = false,nullable = true)
        private Timestamp updated_Date=null;

        @Column(name = "UPDATED_BY", unique = false,nullable = true)
        private String updated_By=null;

        @Transient
        @Column(name = "TOTAL_USER", unique = false,nullable = true)
        private Integer totalUser=0;
}
