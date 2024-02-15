package com.nt.red_distribute_api.dto.resp;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JwtErrorResp {
    private Integer statusCode;
    private String message;
}
