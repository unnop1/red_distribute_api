package com.nt.red_distribute_api.dto.resp;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nt.red_distribute_api.entity.view.consumer_ordertype.ConsumerLJoinOrderType;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerODTDetailResp {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("consumer_group")
    private String consumer_group;
    @JsonProperty("system_name")
    private String system_name;
    @JsonProperty("departmentName")
    private String departmentName;
    @JsonProperty("contactName")
    private String contactName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("is_enable")
    private Integer is_enable=1;
    @JsonProperty("is_delete")
    private Integer is_delete=0;
    @JsonProperty("is_delete_date")
    private Timestamp is_delete_date;
    @JsonProperty("is_delete_by")
    private String is_delete_by;
    @JsonProperty("created_date")
    private Timestamp created_date;
    @JsonProperty("created_by")
    private String created_by;
    @JsonProperty("updated_date")
    private Timestamp updated_date;
    @JsonProperty("updated_by")
    private String updated_by;   
    @JsonProperty("orderTypes")
    private List<ConsumerLJoinOrderType> orderTypes;   
}
