package com.nt.red_distribute_api.dto.req.permission;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateByPermissionReq {
    @JsonProperty("update_id")
    private Long updateID;

    @JsonProperty("update_info")
    private AddPermissionReq updateInfo;
}
