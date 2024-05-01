
package com.nt.red_distribute_api.dto.req.manage_system;

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
public class ListByOrderTypeReq {
    @JsonProperty("draw")
    private Integer draw=11;

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

    @JsonProperty("start")
    private Integer start=0;

    @JsonProperty("length")
    private Integer length=10;

    @JsonProperty("Search")
    private String search="";

    @JsonProperty("Search_field")
    private String searchField="";

    @lombok.NonNull
    @JsonProperty("order_type")
    private String order_Type;
}
