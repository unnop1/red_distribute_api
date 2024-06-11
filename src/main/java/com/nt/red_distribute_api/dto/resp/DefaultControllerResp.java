package com.nt.red_distribute_api.dto.resp;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultControllerResp {
    private int statusCode=200;
    private int count;
    private String message;
    private Object data;
    private Integer draw=11;
    private Integer recordsTotal=0;
    private Integer recordsFiltered=0;
}
