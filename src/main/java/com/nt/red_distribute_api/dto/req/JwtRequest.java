package com.nt.red_distribute_api.dto.req;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JwtRequest {
    private String username;
    private String password;
    private String device;
    private String system;
    private String browser;
}
