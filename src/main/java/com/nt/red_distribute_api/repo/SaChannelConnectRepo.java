package com.nt.red_distribute_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.red_distribute_api.entity.LogLoginEntity;
import com.nt.red_distribute_api.entity.SaChannelConEntity;

public interface SaChannelConnectRepo extends JpaRepository<SaChannelConEntity,Long> {
    // List<LogLoginEntity> getLogLogin();
}
