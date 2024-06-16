package com.nt.red_distribute_api.dto.resp.external;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListConsumeMsg {
    private String err;
    private List<String> messages;
}
