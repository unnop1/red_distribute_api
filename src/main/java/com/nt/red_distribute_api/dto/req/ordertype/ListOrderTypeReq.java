package com.nt.red_distribute_api.dto.req.ordertype;

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
public class ListOrderTypeReq {
    @JsonProperty("page")
    private Integer page=1;

    @JsonProperty("limit")
    private Integer limit=10;

    @JsonProperty("order[0][dir]")
    private String sortBy="ASC";

    @JsonProperty("order[0][name]")
    private String sortName="created_date";

    @lombok.NonNull
    @JsonProperty("start_time")
    private String start_time;

    @lombok.NonNull
    @JsonProperty("end_time")
    private String end_time;

    @JsonProperty("Search")
    private String search="";

    @JsonProperty("Search_field")
    private String searchField="";
}
