package com.nt.red_distribute_api.repo.view.user;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nt.red_distribute_api.entity.UserEntity;
import com.nt.red_distribute_api.entity.view.user.ListUser;

public interface ListUserRepo extends JpaRepository<UserEntity,Long> {
    
    @Query(value = """
                    SELECT u.id, u.username as username, u.email as email, u.is_enable as is_enable, u.name as name , u.departmentname as departmentname, u.created_date as created_date, u.last_login as last_login, sa_pm.permission_name as permission_name
                    FROM user_db u 
                    LEFT JOIN sa_menu_permission sa_pm 
                    ON (u.sa_menu_permission_id = sa_pm.id) 
                """,
                 nativeQuery = true)
    public List<ListUser> getAllUser(Pageable pageable);

    @Query(value = "SELECT  COUNT(*) FROM  user_db ", nativeQuery = true)
    public Integer getTotalCount();

    @Query(value = """
                    SELECT u.id, u.username as username, u.email as email, u.is_enable as is_enable, u.name as name , u.departmentname as departmentname, u.created_date as created_date, u.last_login as last_login, sa_pm.permission_name as permission_name
                    FROM user_db u 
                    LEFT JOIN sa_menu_permission sa_pm 
                    ON (u.sa_menu_permission_id = sa_pm.id)
                    WHERE ( u.username like %:search% OR u.email like %:search% OR u.name like %:search% OR u.departmentname like %:search% OR sa_pm.permission_name like %:search% )
                """,
                 nativeQuery = true)
    public List<ListUser> getListUserAllSearch(@Param(value = "search") String search, Pageable pageable);

    @Query(value = """
                    SELECT  COUNT(*) FROM  user_db u
                    LEFT JOIN sa_menu_permission sa_pm 
                    ON (u.sa_menu_permission_id = sa_pm.id)
                    WHERE ( u.username like %:search% OR u.email like %:search% OR u.name like %:search% OR u.departmentname like %:search% OR sa_pm.permission_name like %:search% )
                    """
                    , nativeQuery = true)
    public Integer getListUserAllSearchTotalCount(@Param(value = "search") String search);


    // permission_name
    @Query(value = """
                    SELECT u.id, u.username as username, u.email as email, u.is_enable as is_enable, u.name as name , u.departmentname as departmentname, u.created_date as created_date, u.last_login as last_login, sa_pm.permission_name as permission_name
                    FROM user_db u 
                    LEFT JOIN sa_menu_permission sa_pm 
                    ON (u.sa_menu_permission_id = sa_pm.id) 
                    WHERE ( sa_pm.permission_name LIKE %:search% )
                """,
                 nativeQuery = true)
    public List<ListUser> getListUserSAPMFieldSearch(
        @Param(value = "search")String search,
        Pageable pageable
    );

    @Query(value = """
                    SELECT  COUNT(*) FROM  user_db u
                    LEFT JOIN sa_menu_permission sa_pm 
                    ON (u.sa_menu_permission_id = sa_pm.id)
                    WHERE ( sa_pm.permission_name LIKE %:search% )
                    """
                    , nativeQuery = true)
    public Integer getListUserSAPMFieldTotalCount(                                          
        @Param(value = "search")String search
    );


    // username
    @Query(value = """
                    SELECT u.id, u.username as username, u.email as email, u.is_enable as is_enable, u.name as name , u.departmentname as departmentname, u.created_date as created_date, u.last_login as last_login, sa_pm.permission_name as permission_name
                    FROM user_db u 
                    LEFT JOIN sa_menu_permission sa_pm 
                    ON (u.sa_menu_permission_id = sa_pm.id) 
                    WHERE ( u.username LIKE %:search% )
                """,
                 nativeQuery = true)
    public List<ListUser> getListUserUsernameFieldSearch(
        @Param(value = "search")String search,
        Pageable pageable
    );

    @Query(value = """
                    SELECT  COUNT(*) FROM  user_db u
                    LEFT JOIN sa_menu_permission sa_pm 
                    ON (u.sa_menu_permission_id = sa_pm.id)
                    WHERE ( u.username LIKE %:search% )
                    """
                    , nativeQuery = true)
    public Integer getListUserUsernameFieldTotalCount(                                          
        @Param(value = "search")String search
    );

    // email
    @Query(value = """
                    SELECT u.id, u.username as username, u.email as email, u.is_enable as is_enable, u.name as name , u.departmentname as departmentname, u.created_date as created_date, u.last_login as last_login, sa_pm.permission_name as permission_name
                    FROM user_db u 
                    LEFT JOIN sa_menu_permission sa_pm 
                    ON (u.sa_menu_permission_id = sa_pm.id) 
                    WHERE ( u.email LIKE %:search% )
                """,
                 nativeQuery = true)
    public List<ListUser> getListUserEmailFieldSearch(
        @Param(value = "search")String search,
        Pageable pageable
    );

    @Query(value = """
                    SELECT  COUNT(*) FROM  user_db u
                    LEFT JOIN sa_menu_permission sa_pm 
                    ON (u.sa_menu_permission_id = sa_pm.id)
                    WHERE ( u.email LIKE %:search% )
                    """
                    , nativeQuery = true)
    public Integer getListUserEmailFieldTotalCount(                                          
        @Param(value = "search")String search
    );

    // departmentname
    @Query(value = """
                    SELECT u.id, u.username as username, u.email as email, u.is_enable as is_enable, u.name as name , u.departmentname as departmentname, u.created_date as created_date, u.last_login as last_login, sa_pm.permission_name as permission_name
                    FROM user_db u 
                    LEFT JOIN sa_menu_permission sa_pm 
                    ON (u.sa_menu_permission_id = sa_pm.id) 
                    WHERE ( u.departmentname LIKE %:search% )
                """,
                 nativeQuery = true)
    public List<ListUser> getListUserdepartmentnameFieldSearch(
        @Param(value = "search")String search,
        Pageable pageable
    );

    @Query(value = """
                    SELECT  COUNT(*) FROM  user_db u
                    LEFT JOIN sa_menu_permission sa_pm 
                    ON (u.sa_menu_permission_id = sa_pm.id)
                    WHERE ( u.departmentname LIKE %:search% )
                    """
                    , nativeQuery = true)
    public Integer getListUserdepartmentnameTotalCount(                                          
        @Param(value = "search")String search
    );

    // name
    @Query(value = """
                    SELECT u.id, u.username as username, u.email as email, u.is_enable as is_enable, u.name as name , u.departmentname as departmentname, u.created_date as created_date, u.last_login as last_login, sa_pm.permission_name as permission_name
                    FROM user_db u 
                    LEFT JOIN sa_menu_permission sa_pm 
                    ON (u.sa_menu_permission_id = sa_pm.id) 
                    WHERE ( u.name LIKE %:search% )
                """,
                 nativeQuery = true)
    public List<ListUser> getListUserNameFieldSearch(
        @Param(value = "search")String search,
        Pageable pageable
    );

    @Query(value = """
                    SELECT  COUNT(*) FROM  user_db u
                    LEFT JOIN sa_menu_permission sa_pm 
                    ON (u.sa_menu_permission_id = sa_pm.id)
                    WHERE ( u.name LIKE %:search% )
                    """
                    , nativeQuery = true)
    public Integer getListUserNameTotalCount(                                          
        @Param(value = "search")String search
    );

}
