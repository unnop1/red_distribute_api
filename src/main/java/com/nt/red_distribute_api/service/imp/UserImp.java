package com.nt.red_distribute_api.service.imp;

import com.nt.red_distribute_api.config.AuthConfig;
import com.nt.red_distribute_api.dto.req.UserRequestDto;
import com.nt.red_distribute_api.dto.resp.LoginResp;
import com.nt.red_distribute_api.dto.resp.UserResp;
import com.nt.red_distribute_api.entity.UserEnitiy;
import com.nt.red_distribute_api.exp.UserAlreadyExistsException;
import com.nt.red_distribute_api.repo.UserRepo;
import com.nt.red_distribute_api.service.UserService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserImp implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthConfig authConfig;
    @Override
    public UserEnitiy loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEnitiy user = userRepo.findByEmail(username);
        // System.out.println("Retrived Data");
        // System.out.println(user.getPassword()+"Retrived Password");
        // System.out.println(user.getUsername());
        // System.out.println(user.getId());
        // System.out.println(user.getEmail());
        // System.out.println("-----");
        return user;
    }

    @Override
    public List<UserResp> getAllUser() {
        List<UserEnitiy> userEnitiys = userRepo.findAll();
        List<UserResp> userResponseDtoList = userEnitiys.stream().map(user->this.userEntityToUserRespDto(user)).collect(Collectors.toList());
        return userResponseDtoList;


    }
    @Override
    public UserResp createUser(UserRequestDto userRequestDto) {
        UserEnitiy foundUser = this.userRepo.findByEmail(userRequestDto.getUsername());
        if (foundUser.getUsername() != null) {
            UserEnitiy user = this.userReqDtoToUserEntity(userRequestDto);
            user.setPassword(authConfig.passwordEncoder().encode(user.getPassword()));
            UserEnitiy createdUser = userRepo.save(user);
            return this.userEntityToUserRespDto(createdUser);
        } else {
            // User already exists, throw an exception
            throw new UserAlreadyExistsException("User with email " + userRequestDto.getUsername() + " already exists");
        }
    }

    @Override
    public void updateUser(String email, HashMap<String, Object> updateInfo) {
        UserEnitiy foundUser = this.userRepo.findByEmail(email);
        System.out.println("foundUser:"+foundUser.getUsername());
        if (foundUser.getUsername() != null) {
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


    public UserEnitiy userReqDtoToUserEntity(UserRequestDto userReqDto){
        UserEnitiy user = this.modelMapper.map(userReqDto,UserEnitiy.class);
        return user;
    }
    public UserResp userEntityToUserRespDto(UserEnitiy user){
        UserResp userRespDto = this.modelMapper.map(user,UserResp.class);
        return userRespDto;
    }
    
}
