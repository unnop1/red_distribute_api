package com.nt.red_distribute_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.red_distribute_api.entity.LogLoginEntity;

public interface LogLoginRepo extends JpaRepository<LogLoginEntity,Long> {
    // List<LogLoginEntity> getLogLogin();
}
