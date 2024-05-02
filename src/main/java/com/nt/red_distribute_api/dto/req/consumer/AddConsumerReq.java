package com.nt.red_distribute_api.dto.req.consumer;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddConsumerReq {
    @JsonProperty("username")
    private String username="";

    @JsonProperty("system_name")
    private String system_name="";

    @JsonProperty("password")
    private String password="";

    @JsonProperty("department_name")
    private String department_name="";

    @JsonProperty("contact_name")
    private String contact_name="";

    @JsonProperty("email")
    private String email="";

    @JsonProperty("phone_number")
    private String phone_number="";

    @JsonProperty("order_type_topics")
    private List<String> order_type_topics=null;

}
