package com.nt.red_distribute_api.repo;

import com.nt.red_distribute_api.enitiy.PermissionMenuEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PermissionMenuRepo extends JpaRepository<PermissionMenuEntity,Long> {
    
    @Query(value = "SELECT * FROM sa_menu_permission", nativeQuery = true)
    public List<PermissionMenuEntity> findAll();

}
