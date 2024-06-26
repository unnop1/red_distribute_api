package com.nt.red_distribute_api.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaListTopics {

    // @JsonProperty("pageCount")
    // private Integer pageCount;

    @JsonProperty("topics")
    private Object topics;
}
