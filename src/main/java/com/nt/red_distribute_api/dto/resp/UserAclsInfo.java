package com.nt.red_distribute_api.dto.resp;

import org.apache.kafka.common.acl.AclOperation;
import org.apache.kafka.common.acl.AclPermissionType;
import org.apache.kafka.common.resource.PatternType;
import org.apache.kafka.common.resource.ResourceType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserAclsInfo {
    @JsonProperty("name")
    private String name;

    @JsonProperty("resource_type")
    private ResourceType resource_type;

    @JsonProperty("pattern_type")
    private PatternType pattern_type;

    @JsonProperty("principal")
    private String principal;

    @JsonProperty("host")
    private String host;

    @JsonProperty("operation")
    private AclOperation operation;

    @JsonProperty("permission_type")
    private AclPermissionType permission_type;

}
