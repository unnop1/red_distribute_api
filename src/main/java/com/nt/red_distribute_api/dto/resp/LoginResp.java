package com.nt.red_distribute_api.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.JSONPObject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter
public class LoginResp {
    @JsonProperty("user")
    @Column(name = "user")
    private UserResp userLogin;

    @JsonProperty("access_token")
    @Column(name = "access_token")
    private String jwtToken;

    @JsonProperty("permission_menu")
    @Column(name = "permission_menu")
    private String permissionJson;

    @JsonProperty("permissionName") 
    @Column(name = "permissionName")   
    private String permissionName;
}
