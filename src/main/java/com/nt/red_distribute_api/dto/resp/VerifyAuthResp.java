package com.nt.red_distribute_api.dto.resp;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.nt.red_distribute_api.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyAuthResp {
    @JsonProperty("username")
    private String username=null;

    @JsonProperty("email")
    private String email=null;

    @JsonProperty("device")
    private String device=null;

    @JsonProperty("browser")
    private String browser=null;

    @JsonProperty("system")
    private String system=null;

    @JsonProperty("userInfo")
    private UserEntity userInfo=null;

    @JsonProperty("error")
    private String error=null;
}
