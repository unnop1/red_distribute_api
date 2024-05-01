package com.nt.red_distribute_api.dto.req.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDto {
        @JsonProperty("user_id")
        private Long id;

        @JsonProperty("name")
        private String name="";

        @JsonProperty("about_me")
        private String aboutMe="";
        
        @JsonProperty("email")
        private String email="";

        @JsonProperty("is_enable")
        private Integer is_enable=null;

        @JsonProperty("phonenumber")
        private String phonenumber="";

        @JsonProperty("password")
        private String password="";

        @JsonProperty("departmentname")
        private String departmentName="";

        @JsonProperty("sa_menu_permission_id")
        private Long permissionID=null;
}
