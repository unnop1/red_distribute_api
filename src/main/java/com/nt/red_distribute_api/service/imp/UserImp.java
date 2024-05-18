package com.nt.red_distribute_api.service.imp;

import com.nt.red_distribute_api.config.AuthConfig;
import com.nt.red_distribute_api.dto.req.user.ListUserReq;
import com.nt.red_distribute_api.dto.req.user.UpdateUserDto;
import com.nt.red_distribute_api.dto.req.user.UserRequestDto;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.dto.resp.UserResp;
import com.nt.red_distribute_api.entity.UserEntity;
import com.nt.red_distribute_api.entity.view.user.ListUser;
import com.nt.red_distribute_api.exp.UserAlreadyExistsException;
import com.nt.red_distribute_api.repo.UserRepo;
import com.nt.red_distribute_api.repo.view.user.ListUserRepo;
import com.nt.red_distribute_api.service.UserService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.nt.red_distribute_api.Util.DateTime;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserImp implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ListUserRepo listUserRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthConfig authConfig;

    @Override
    public UserResp findUserById(Long userID) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByID(userID);
        UserResp userResp = userEntityToUserRespDto(user);
        return userResp;
    }

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByUsername(username);
        return user;
    }

    @Override
    public PaginationDataResp getAllUser(ListUserReq req) {
        PaginationDataResp resp = new PaginationDataResp();
        Integer offset = req.getStart();
        Integer limit = req.getLength();
        Integer page = offset / limit;
        String sortName = req.getSortName();
        String sortBy = req.getSortBy();
        String search = req.getSearch();
        String searchField = req.getSearchField().toLowerCase();
        if (sortName.toLowerCase().equals("permission_name")){
            sortName = "sa_pm.permission_name";
        }

        JpaSort sort = JpaSort.unsafe(Sort.Direction.fromString(sortBy), sortName);

        if ( search.isEmpty()){
            List<ListUser> userResponseDtoList = listUserRepo.getAllUser(PageRequest.of(page, limit, sort));
            Integer count = listUserRepo.getTotalCount();
            resp.setData(userResponseDtoList);
            resp.setCount(count);
            return resp;
        }else {
            
            if( !req.getSearchField().isEmpty()){
                if (searchField.equals("permission_name")){
                    List<ListUser> userResponseDtoList = listUserRepo.getListUserSAPMFieldSearch(search, PageRequest.of(page, limit, sort));
                    Integer count = listUserRepo.getListUserSAPMFieldTotalCount(search);
                    resp.setCount(count);
                    resp.setData(userResponseDtoList);
                    return resp;
                }else if (searchField.equals("username")){
                    List<ListUser> userResponseDtoList = listUserRepo.getListUserUsernameFieldSearch(search, PageRequest.of(page, limit, sort));
                    Integer count = listUserRepo.getListUserUsernameFieldTotalCount(search);
                    resp.setCount(count);
                    resp.setData(userResponseDtoList);
                    return resp;
                }else if (searchField.equals("name")){
                    List<ListUser> userResponseDtoList = listUserRepo.getListUserNameFieldSearch(search, PageRequest.of(page, limit, sort));
                    Integer count = listUserRepo.getListUserNameTotalCount(search);
                    resp.setCount(count);
                    resp.setData(userResponseDtoList);
                    return resp;
                }else if (searchField.equals("email")){
                    List<ListUser> userResponseDtoList = listUserRepo.getListUserEmailFieldSearch(search, PageRequest.of(page, limit, sort));
                    Integer count = listUserRepo.getListUserEmailFieldTotalCount(search);
                    resp.setCount(count);
                    resp.setData(userResponseDtoList);
                    return resp;
                }else if (searchField.equals("departmentname")){
                    List<ListUser> userResponseDtoList = listUserRepo.getListUserdepartmentnameFieldSearch(search, PageRequest.of(page, limit, sort));
                    Integer count = listUserRepo.getListUserdepartmentnameTotalCount(search);
                    resp.setCount(count);
                    resp.setData(userResponseDtoList);
                    return resp;
                }

                
            }
            System.out.println("all");
            List<ListUser> userResponseDtoList = listUserRepo.getListUserAllSearch(search, PageRequest.of(page, limit, sort));
                Integer count = listUserRepo.getListUserAllSearchTotalCount(search);
                resp.setCount(count);
                resp.setData(userResponseDtoList);
                return resp;
        }


    }
    @Override
    public UserResp createUser(UserRequestDto req, String createdBy) {
        UserEntity foundUser = this.userRepo.loadByUniqueUser(req.getEmail(), req.getUsername());
        if (foundUser == null) {
            // Creat a new user
            Timestamp timeNow = DateTime.getTimeStampNow();
            UserEntity user = new UserEntity();
            user.setAbout_me(req.getAboutMe());
            user.setDepartmentname(req.getDepartmentName());
            user.setEmail(req.getEmail());
            user.setUsername(req.getUsername());
            user.setName(req.getName());
            user.setPhoneNumber(req.getPhonenumber());
            user.setCreated_by(createdBy);
            user.setCreated_Date(timeNow);
            // Encode password  
            user.setPassword(authConfig.passwordEncoder().encode(req.getPassword()));
            // Set permissions
            user.setSa_menu_permission_id(req.getPermissionID());
            

            UserEntity createdUser = userRepo.save(user);

            return this.userEntityToUserRespDto(createdUser);
        } else {
            // User already exists, throw an exception
            throw new UserAlreadyExistsException("User with email " + req.getUsername() + " already exists");
        }
    }
    
    public UserResp userEntityToUserRespDto(UserEntity user){
        UserResp userRespDto = this.modelMapper.map(user,UserResp.class);
        return userRespDto;
    }

    @Override
    public UserEntity findUserLogin(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findLoginUser(username);
        return user;
    }

    @Override
    public void updateUserLogLogin(Long userID, HashMap<String, Object> updateInfo) {
        UserEntity foundUser = this.userRepo.findByID(userID);
        System.out.println("foundUser:"+foundUser.getUsername());
        if (foundUser != null) {
            for (Map.Entry<String, Object> entry : updateInfo.entrySet()) {
                String fieldName = entry.getKey();
                Object fieldValue = entry.getValue();
                System.out.println("before fieldName:"+fieldName+" value:"+fieldValue);
                // Update the field if it exists in the UserEntity class
                try {
                    Field field = foundUser.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(foundUser, fieldValue);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
                System.out.println("after fieldName:"+fieldName+" value:"+fieldValue);
            }
            
            userRepo.save(foundUser);
        }
    }

    @Override
    public void updateUser(UpdateUserDto req, String updatedBy) {
        UserEntity foundUser = this.userRepo.findByID(req.getId());
        Timestamp timeNow = DateTime.getTimeStampNow();
        if (foundUser != null){
            System.out.println("found user: " + foundUser.getId());

            if (!req.getName().isEmpty()){
                foundUser.setName(req.getName());
            }
            if (!req.getDepartmentName().isEmpty()){
                foundUser.setDepartmentname(req.getDepartmentName());
            }
            if (!req.getPhonenumber().isEmpty()){
                foundUser.setPhoneNumber(req.getPhonenumber());
            }
            if (!req.getEmail().isEmpty()){
                foundUser.setEmail(req.getEmail());
            }
            if (req.getIs_enable() != null){
                foundUser.setIs_Enable(req.getIs_enable());
            }
            if (!req.getPassword().isEmpty()){
                foundUser.setPassword(authConfig.passwordEncoder().encode(req.getPassword()));
            }
            if (req.getPermissionID() != null){
                foundUser.setSa_menu_permission_id(req.getPermissionID());
            }
            
            foundUser.setUpdated_Date(timeNow);
            foundUser.setUpdated_by(updatedBy);
            
            userRepo.save(foundUser);
        }else{
            System.out.println("not found user");
        }

        return;
        
    }

    @Override
    public Void removeUser(Long userID) {
        userRepo.deleteById(userID);
        return null;
    }
    
}