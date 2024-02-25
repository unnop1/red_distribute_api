package com.nt.red_distribute_api.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
        private String name=null;
        private String email;
        private String password=null;
        private String aboutMe=null;
}
