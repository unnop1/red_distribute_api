package com.nt.red_distribute_api.dto.resp;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nt.red_distribute_api.entity.UserEnitiy;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResp {
    @JsonProperty("user")
    private UserResp userLogin;

    @JsonProperty("access_token")
    private String jwtToken;

    @JsonProperty("permission_menu")
    private Object permissionJson;

    @JsonProperty("permissionName")    
    private String permissionName;
}
