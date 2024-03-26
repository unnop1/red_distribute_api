package com.nt.red_distribute_api.dto.resp;


import java.security.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResp {
    private long id;
    private String name;
    private String email;
    private String aboutMe;
    private Timestamp last_login;
    private String last_login_ipaddress;
    private boolean isDelete;
    private String isDelete_by;
    private Timestamp isDelete_date;
    private Timestamp createdDate;
    private String created_by;
    private Timestamp updatedDate;
    private String updated_by;
}
