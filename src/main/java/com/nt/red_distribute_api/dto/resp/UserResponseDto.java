package com.nt.red_distribute_api.dto.resp;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    @Column(name = "name", unique = false,nullable = true)
    private String name;

    @Column(name = "email", unique = true,nullable = false)
    private String email;

    @Column(name = "about_me", unique = false,nullable = true)
    private String aboutMe;
}
