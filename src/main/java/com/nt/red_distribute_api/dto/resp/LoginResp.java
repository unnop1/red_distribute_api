package com.nt.red_distribute_api.dto.resp;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResp {
    @Column(name = "email", unique = true,nullable = false)
    private String email;

    @JsonProperty("token")
    private String jwtToken;

    @Column(name = "permission_json", unique = false,nullable = true)
    @Formula("(SELECT smp.permission_json\n" + //
                "FROM user_db as ud\n" + //
                "JOIN sa_menu_permission as smp ON ud.id = smp.user_id;\n" + //
            ")")
    private String permissionJson;
}
