package com.nt.red_distribute_api.dto.resp;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AuthSuccessResp {
    private String token;
}
