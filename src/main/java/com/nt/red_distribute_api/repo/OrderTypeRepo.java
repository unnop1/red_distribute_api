package com.nt.red_distribute_api.repo;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nt.red_distribute_api.entity.OrderTypeEntity;
import com.nt.red_distribute_api.entity.view.order_type.OrderTypeDashboardTrigger;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTypeRepo extends JpaRepository<OrderTypeEntity,Long> {

    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM order_type WHERE TYPEID=?1", nativeQuery = true)
    public Optional<OrderTypeEntity> findById(Long orderTypeID);

    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM order_type WHERE MAINID=?1", nativeQuery = true)
    public OrderTypeEntity findByMainId(Long orderTypeMainID);

    @Query(value = """
                SELECT 
                    odt.*,
                    (SELECT COUNT(id) FROM consumer_ordertype cod WHERE cod.ordertype_id = odt.id) AS TotalConsumer,
                    (SELECT COUNT(id) FROM trigger_message trg WHERE trg.ordertype_id = odt.id) AS TotalTrigger
                FROM 
                    ordertype odt
                LEFT JOIN sa_channel_connect sac
                ON sac.id = odt.SA_CHANNEL_CONNECT_ID
                   """,
                 nativeQuery = true)
    public List<OrderTypeDashboardTrigger> OrderTypeTriggerDashboard(
      Pageable pageable
    );

    @Query(value = """
                SELECT 
                    odt.*,
                    (SELECT COUNT(id) FROM consumer_ordertype cod WHERE cod.ordertype_id = odt.id) AS TotalConsumer
                FROM 
                    ordertype odt
                   """,
                 nativeQuery = true)
    public List<OrderTypeDashboardTrigger> ListManageOrderType(
      Pageable pageable
    );

    @Query(value = """
                SELECT 
                    COUNT(*)
                FROM 
                    ordertype
                   """,
                 nativeQuery = true)
    public Integer getListManageOrderTypeTotal();

    @Query(value = """
                SELECT 
                    odt.*,
                    (SELECT COUNT(id) FROM consumer_ordertype cod WHERE cod.ordertype_id = odt.id) AS TotalConsumer,
                    (SELECT COUNT(id) FROM trigger_message trg WHERE trg.ordertype_id = odt.id) AS TotalTrigger
                FROM 
                    ordertype odt
                LEFT JOIN sa_channel_connect sac
                ON sac.id = odt.SA_CHANNEL_CONNECT_ID
                WHERE odt.SA_CHANNEL_CONNECT_ID = :channel_id
                   """,
                 nativeQuery = true)
    public List<OrderTypeDashboardTrigger> OrderTypeTriggerDashboardByChannelID(
    //   @Param(value = "start_time") Timestamp startTime,
    //   @Param(value = "end_time") Timestamp endTime,
      @Param(value = "channel_id") Long channelID,
      Pageable pageable
    );

}
