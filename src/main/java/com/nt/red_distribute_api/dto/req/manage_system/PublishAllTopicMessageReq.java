package com.nt.red_distribute_api.dto.req.manage_system;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublishAllTopicMessageReq {
    @JsonProperty("message")
    private Object message;

}
