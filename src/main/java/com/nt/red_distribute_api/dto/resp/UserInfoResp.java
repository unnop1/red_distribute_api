package com.nt.red_distribute_api.dto.resp;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nt.red_distribute_api.entity.UserEntity;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResp {
    @JsonProperty("user")
    private UserResp userLogin;

    @JsonProperty("permission_menu")
    private Object permissionJson;

    @JsonProperty("permissionName")    
    private String permissionName;
}
