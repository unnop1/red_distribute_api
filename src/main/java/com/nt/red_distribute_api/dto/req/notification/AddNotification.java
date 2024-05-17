package com.nt.red_distribute_api.dto.req.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddNotification {
    @JsonProperty("action")
    private String action;

    @JsonProperty("email")
    private String email;

    @JsonProperty("message")
    private String message;
}
