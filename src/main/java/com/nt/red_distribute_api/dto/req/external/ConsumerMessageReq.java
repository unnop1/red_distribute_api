package com.nt.red_distribute_api.dto.req.external;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerMessageReq {
    @JsonProperty("topic_name")
    private String topicName;

    @JsonProperty("offset")
    private long offset=0;

    @JsonProperty("limit")
    private Integer limit=10;

}
