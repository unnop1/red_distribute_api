package com.nt.red_distribute_api.dto.resp.external;

import com.nt.red_distribute_api.entity.ConsumerEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyConsumerResp {
    private ConsumerEntity consumerData;
    private Boolean isVerify=false;
    private String remark;
    
}
