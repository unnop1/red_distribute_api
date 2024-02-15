package com.nt.red_distribute_api.repo;

import com.nt.red_distribute_api.enitiy.OrderTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderTypeRepo extends JpaRepository<OrderTypeEntity,Long> {
    
    @Query(value = "SELECT odt.*, (SELECT COUNT(sms_gw.GID) FROM sms_gateway AS sms_gw WHERE odt.MainID = sms_gw.order_type_MainID) as TotleMsg, (SELECT COUNT(sms_gw.GID) FROM sms_gateway AS sms_gw WHERE odt.MainID = sms_gw.order_type_MainID AND IsStatus = 1) as TotleSend FROM order_type AS odt WHERE IsDelete = 0", nativeQuery = true)
    public List<OrderTypeEntity> findAll();

}
