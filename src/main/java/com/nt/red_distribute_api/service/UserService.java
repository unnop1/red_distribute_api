package com.nt.red_distribute_api.service;

import com.nt.red_distribute_api.dto.req.UserRequestDto;
import com.nt.red_distribute_api.dto.resp.LoginResp;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<LoginResp> getAllUser();
    public LoginResp createUser(UserRequestDto userRequestDto);
}
