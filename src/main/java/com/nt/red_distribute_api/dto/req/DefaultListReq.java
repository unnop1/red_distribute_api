package com.nt.red_distribute_api.dto.req;


import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DefaultListReq {
    @JsonProperty("draw")
    private Integer draw=1;

    @JsonProperty("order[0][dir]")
    private String sortBy="ASC";

    @JsonProperty("order[0][name]")
    private String sortName="created_date";

    @lombok.NonNull
    @JsonProperty("start_time")
    private String start_time=null;

    @lombok.NonNull
    @JsonProperty("end_time")
    private String end_time=null;

    @JsonProperty("start")
    private Integer start=0;

    @JsonProperty("length")
    private Integer length=10;

    @JsonProperty("Search")
    private String search="";

    @JsonProperty("Search_field")
    private String searchField="";
}
