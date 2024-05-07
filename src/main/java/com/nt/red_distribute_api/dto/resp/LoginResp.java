package com.nt.red_distribute_api.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
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
