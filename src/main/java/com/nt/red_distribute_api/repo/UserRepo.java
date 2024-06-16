package com.nt.red_distribute_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nt.red_distribute_api.entity.UserEntity;


public interface UserRepo extends JpaRepository<UserEntity,Long> {
    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM user_db WHERE ID=?1", nativeQuery = true)
    public UserEntity findByID(Long userID);

    @Query(value = "SELECT * FROM user_db WHERE ID IN (?1)", nativeQuery = true)
    public List<UserEntity> findInID(List<Long> userIDs);

    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM user_db WHERE EMAIL=?1", nativeQuery = true)
    public UserEntity findByEmail(String email);

    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM user_db WHERE USERNAME=?1", nativeQuery = true)
    public UserEntity findByUsername(String username);

     
    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM user_db WHERE EMAIL=?1 AND USERNAME=?2", nativeQuery = true)
    public UserEntity loadByUniqueUser(String email, String username);

    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM user_db WHERE EMAIL=?1 OR USERNAME=?1 AND IS_ENABLE=1", nativeQuery = true)
    public UserEntity findLoginUser(String username);

}
