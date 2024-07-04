package com.nt.red_distribute_api.dto.resp.external;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicCount {
    private long count;
    private Map<String, Object> partition;
}
