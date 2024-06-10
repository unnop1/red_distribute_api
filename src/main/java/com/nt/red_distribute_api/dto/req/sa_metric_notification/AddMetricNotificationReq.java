package com.nt.red_distribute_api.dto.req.sa_metric_notification;

import java.sql.Clob;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMetricNotificationReq {
    @JsonProperty("email")
    private String email = null;

    @JsonProperty("om_not_connect")
    private Integer OM_NOT_CONNECT = null;

    @JsonProperty("db_om_not_connect")
    private Integer DB_OM_NOT_CONNECT = null;

    @JsonProperty("topup_not_connect")
    private Integer TOPUP_NOT_CONNECT = null;

    @JsonProperty("trigger_noti_json")
    private String TRIGGER_NOTI_JSON = null;

    @JsonProperty("line_is_active")
    private Integer LINE_IS_ACTIVE = null;

    @JsonProperty("line_token")
    private String LINE_TOKEN = null;

}
