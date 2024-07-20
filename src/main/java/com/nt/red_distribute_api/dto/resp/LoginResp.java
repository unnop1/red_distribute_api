package com.nt.red_distribute_api.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class LoginResp {
    @JsonProperty("user")
    private UserResp userLogin;

    // @JsonProperty("ip")
    // private String ip;

    @JsonProperty("access_token")
    private String jwtToken;

    @JsonProperty("permission_menu")
    private String permissionJson;

    @JsonProperty("permissionName")    
    private String permissionName;
}
