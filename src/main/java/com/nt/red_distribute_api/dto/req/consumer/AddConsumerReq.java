package com.nt.red_distribute_api.dto.req.consumer;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddConsumerReq {
    @JsonProperty("username")
    private String username=null;

    @JsonProperty("system_name")
    private String system_name=null;

    @JsonProperty("password")
    private String password=null;

    @JsonProperty("department_name")
    private String department_name=null;

    @JsonProperty("contact_name")
    private String contact_name=null;

    @JsonProperty("email")
    private String email=null;

    @JsonProperty("is_enable")
    private Integer is_enable=null;

    @JsonProperty("is_delete")
    private Integer is_delete=null;

    @JsonProperty("phone_number")
    private String phone_number=null;

    @JsonProperty("order_type_ids")
    private List<Long> order_type_ids=null;

}
