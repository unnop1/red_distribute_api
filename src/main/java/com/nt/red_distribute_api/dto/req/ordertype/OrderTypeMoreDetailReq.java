package com.nt.red_distribute_api.dto.req.ordertype;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderTypeMoreDetailReq {
    @JsonProperty("Ordertype_id")
    private Long Ordertype_id;
}
