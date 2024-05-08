package com.nt.red_distribute_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nt.red_distribute_api.entity.SaMetricNotificationEntity;

public interface SaMetricNotificationRepo extends JpaRepository<SaMetricNotificationEntity,Long> {
    
}
