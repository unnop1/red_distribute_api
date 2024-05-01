package com.nt.red_distribute_api.dto.req.kafka;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class TopicReq {
    private String topicName;
    private Integer partitions=1;
    private String retentionMs="86400000";
    private short replicationFactor=3;
}
