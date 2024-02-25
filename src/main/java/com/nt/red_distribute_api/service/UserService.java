package com.nt.red_distribute_api.service;

import com.nt.red_distribute_api.dto.req.UserRequestDto;
import com.nt.red_distribute_api.dto.resp.LoginResp;
import com.nt.red_distribute_api.enitiy.UserEnitiy;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.List;

public interface UserService extends UserDetailsService {
    List<LoginResp> getAllUser();
    public LoginResp createUser(UserRequestDto userRequestDto);
    void updateUser(String email, HashMap<String, Object> updateInfo);
    UserEnitiy loadUserByUsername(String username) throws UsernameNotFoundException;
}
