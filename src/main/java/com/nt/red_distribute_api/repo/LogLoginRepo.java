package com.nt.red_distribute_api.repo;

import com.nt.red_distribute_api.enitiy.LogLoginEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogLoginRepo extends JpaRepository<LogLoginEntity,Long> {
    // List<LogLoginEntity> getLogLogin();
}
