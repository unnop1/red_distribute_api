package com.nt.red_distribute_api.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaListTopicsResp {

    @JsonProperty("data")
    private KafkaListTopics topics;

    @JsonProperty("error")
    private String error;
}
