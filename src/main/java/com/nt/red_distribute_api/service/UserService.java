package com.nt.red_distribute_api.service;

import com.nt.red_distribute_api.dto.req.user.ListUserReq;
import com.nt.red_distribute_api.dto.req.user.UpdateUserDto;
import com.nt.red_distribute_api.dto.req.user.UserRequestDto;
import com.nt.red_distribute_api.dto.resp.LoginResp;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.dto.resp.UserInfoResp;
import com.nt.red_distribute_api.dto.resp.UserResp;
import com.nt.red_distribute_api.entity.UserEntity;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;

public interface UserService extends UserDetailsService {
    PaginationDataResp getAllUser(ListUserReq req);
    public UserResp createUser(UserRequestDto userRequestDto, String createdBy);
    void updateUser(UpdateUserDto updateUser, String updatedBy);
    void updateUserLogLogin(Long userID, HashMap<String, Object> updates);
    Void removeUser(Long userID);
    // UserEnitiy loadUserByEmail(String email) throws UsernameNotFoundException;
    UserEntity loadUserByUsername(String username) throws UsernameNotFoundException;
    UserEntity findUserLogin(String username) throws UsernameNotFoundException;
    UserResp findUserById(Long userID) throws UsernameNotFoundException;
}
