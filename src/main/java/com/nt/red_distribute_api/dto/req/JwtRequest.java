package com.nt.red_distribute_api.dto.req;


import lombok.*;

@Getter
@Setter
@ToString
public class JwtRequest {
    private String username;
    private String password;
    private String device="Unknown device";
    private String system="Unknown system";
    private String browser="Unknown browser";
}
