package com.nt.red_distribute_api.dto.req.sa_metric_notification;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMetricReq {
    @JsonProperty("update_id")
    private Long updateID;

    @JsonProperty("update_info")
    private AddMetricNotificationReq updateInfo;
}
