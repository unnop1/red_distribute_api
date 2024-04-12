package com.nt.red_distribute_api.service;

import com.nt.red_distribute_api.dto.req.UserRequestDto;
import com.nt.red_distribute_api.dto.resp.UserResp;
import com.nt.red_distribute_api.entity.UserEnitiy;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserResp> getAllUser();
    public UserResp createUser(UserRequestDto userRequestDto);
    void updateUser(Long userID, HashMap<String, Object> updateInfo);
    // UserEnitiy loadUserByEmail(String email) throws UsernameNotFoundException;
    UserEnitiy loadUserByUsername(String username) throws UsernameNotFoundException;
    UserEnitiy findUserLogin(String username) throws UsernameNotFoundException;
}
