package com.nt.red_distribute_api.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nt.red_distribute_api.entity.SaMetricNotificationEntity;

public interface SaMetricNotificationRepo extends JpaRepository<SaMetricNotificationEntity,Long> {
    @Query(value = "SELECT * FROM sa_metric_notification ", nativeQuery = true)
    public List<SaMetricNotificationEntity> ListSaMetrics(Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM sa_metric_notification ", nativeQuery = true)
    public Integer getTotalCount();
}
