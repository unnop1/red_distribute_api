package com.nt.red_distribute_api.dto.resp.external;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumeMessage {
    private Long offset;
    private String key;
    private String value;
}
