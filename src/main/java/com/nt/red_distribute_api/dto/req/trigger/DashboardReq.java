package com.nt.red_distribute_api.dto.req.trigger;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DashboardReq {

    @JsonProperty("by_type")
    private String byType="all";

    @JsonProperty("order[0][dir]")
    private String sortBy="ASC";

    @JsonProperty("order[0][name]")
    private String sortName="created_date";

    @JsonProperty("start_time")
    private String startTime="";

    @JsonProperty("end_time")
    private String endTime="";
}
