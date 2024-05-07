package com.nt.red_distribute_api.dto.req.ordertype;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddOrderTypeReq {
    @JsonProperty("main_id")
    private Long main_id;

    @JsonProperty("channel_id")
    private Long channel_id;

    @JsonProperty("order_type_name")
    private String order_type_name=null;

    @JsonProperty("description")
    private String description=null;

    @JsonProperty("message_expire")
    private String message_expire=null;

    @JsonProperty("is_enable")
    private Integer is_enable=null;

    @JsonProperty("is_delete")
    private Integer is_delete=null;

}
