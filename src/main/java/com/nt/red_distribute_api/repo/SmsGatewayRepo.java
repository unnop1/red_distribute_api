package com.nt.red_distribute_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.red_distribute_api.enitiy.SmsGatewayEntity;

public interface SmsGatewayRepo extends JpaRepository<SmsGatewayEntity,Long> {
    @Query(value = "SELECT * FROM sms_gateway WHERE order_type_MainID=?1 AND IsStatus=?2 ORDER BY CreatedDate DESC ", nativeQuery = true)
    public List<SmsGatewayEntity> findSmsGatewayByOrderTypeAndIsStatus(@Param("order_type_MainID") String orderTypeID, @Param("IsStatus") String isStatus);
}
