package com.nt.red_distribute_api.dto.req.auth;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@ToString
public class JwtRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("device")
    private String device="Unknown device";
    @JsonProperty("system")
    private String system="Unknown system";
    @JsonProperty("browser")
    private String browser="Unknown browser";
    @JsonProperty("ip_address")
    private String ipAddress="Unknown ip_address";
}
