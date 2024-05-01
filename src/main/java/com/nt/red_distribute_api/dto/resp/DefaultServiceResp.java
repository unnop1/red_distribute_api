package com.nt.red_distribute_api.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DefaultServiceResp {
    @JsonProperty("result")
    private Object result = null;

    @JsonProperty("count")
    private Integer count = 0;

    @JsonProperty("message")
    private String message = "Successfully";

    @JsonProperty("error")
    private String error = null;
}
