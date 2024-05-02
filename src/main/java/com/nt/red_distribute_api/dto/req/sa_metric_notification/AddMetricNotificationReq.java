package com.nt.red_distribute_api.dto.req.sa_metric_notification;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMetricNotificationReq {
    @JsonProperty("email")
    private String email = null;

    @JsonProperty("OM_NOT_CONNECT")
    private Integer OM_NOT_CONNECT = null;

    @JsonProperty("DB_OM_NOT_CONNECT")
    private Integer DB_OM_NOT_CONNECT = null;

    @JsonProperty("TOPUP_NOT_CONNECT")
    private Integer TOPUP_NOT_CONNECT = null;

    @JsonProperty("TRIGGER_NOTI_JSON")
    private String TRIGGER_NOTI_JSON = null;

    @JsonProperty("UPDATED_DATE")
    private String UPDATED_DATE = null;

    @JsonProperty("UPDATED_By")
    private String UPDATED_By = null;

}
