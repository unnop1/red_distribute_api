package com.nt.red_distribute_api.dto.resp;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResp {
    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("username")
    private String username;

    @JsonProperty("departmentname")
    private String departmentname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("about_Me")
    private String about_Me;

    @JsonProperty("last_login")
    private Timestamp last_login;

    @JsonProperty("last_login_ipaddress")
    private String last_login_ipaddress;

    @JsonProperty("is_Enable")
    private Integer is_Enable;

    @JsonProperty("is_Delete")
    private Integer is_Delete;

    @JsonProperty("is_Delete_by")
    private String is_Delete_by;

    @JsonProperty("is_Delete_date")
    private Timestamp is_Delete_date;

    @JsonProperty("created_Date")
    private Timestamp created_Date;

    @JsonProperty("created_by")
    private String created_by;

    @JsonProperty("updated_Date")
    private Timestamp updated_Date;

    @JsonProperty("updated_by")
    private String updated_by;

    @JsonProperty("sa_menu_permission_id")
    private Long sa_menu_permission_id;
}
