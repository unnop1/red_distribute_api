package com.nt.red_distribute_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nt.red_distribute_api.entity.UserEnitiy;


public interface UserRepo extends JpaRepository<UserEnitiy,Long> {
    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM user_db WHERE id=?1", nativeQuery = true)
    public UserEnitiy findByID(Long userID);

    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM user_db WHERE email=?1", nativeQuery = true)
    public UserEnitiy findByEmail(String email);

    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM user_db WHERE username=?1", nativeQuery = true)
    public UserEnitiy findByUsername(String username);

     
    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM user_db WHERE email=?1 AND username=?2", nativeQuery = true)
    public UserEnitiy loadByUniqueUser(String email, String username);

    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM user_db WHERE email=?1 OR username=?1", nativeQuery = true)
    public UserEnitiy findLoginUser(String username);

}
