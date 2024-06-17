package com.nt.red_distribute_api.entity.view.permission;


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
        @Column(name = "id")
        private Long id;

        @Column(name = "permission_Name", unique = false,nullable = true)
        private String permission_Name=null;

        @Lob
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

        @Transient
        @Column(name = "totalUser", unique = false,nullable = true)
        private Integer totalUser=0;
}
