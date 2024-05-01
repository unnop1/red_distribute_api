package com.nt.red_distribute_api.dto.resp;


import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResp {
    private long id;
    private String name;
    private String phoneNumber;
    private String username;
    private String email;
    private String about_Me;
    private Timestamp last_login;
    private String last_login_ipaddress;
    private Integer is_Enable;
    private Integer is_Delete;
    private String is_Delete_by;
    private Timestamp is_Delete_date;
    private Timestamp created_Date;
    private String created_by;
    private Timestamp updated_Date;
    private String updated_by;
    private Long sa_menu_permission_id;
}
