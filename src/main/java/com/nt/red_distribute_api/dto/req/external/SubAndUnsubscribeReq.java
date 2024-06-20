package com.nt.red_distribute_api.dto.req.external;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubAndUnsubscribeReq {
    @JsonProperty("topic_name")
    private String topicName="all";
}
