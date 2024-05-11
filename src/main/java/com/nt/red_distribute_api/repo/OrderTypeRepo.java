package com.nt.red_distribute_api.repo;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nt.red_distribute_api.entity.ConsumerEntity;
import com.nt.red_distribute_api.entity.OrderTypeEntity;
import com.nt.red_distribute_api.entity.view.order_type.OrderTypeDashboard;
import com.nt.red_distribute_api.entity.view.order_type.OrderTypeDashboardTrigger;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTypeRepo extends JpaRepository<OrderTypeEntity,Long> {

    @Query(value = """
                  SELECT * 
                  FROM 
                      ordertype
                  WHERE ordertype_name=?1
                   """,
                 nativeQuery = true)
    public OrderTypeEntity OrderTypeByName(String orderTypeName);

    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM ordertype WHERE ID=?1", nativeQuery = true)
    public Optional<OrderTypeEntity> findById(Long orderTypeID);

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
                odt.id, odt.ordertype_name, odt.message_expire, odt.is_delete, odt.is_enable,
                    (SELECT COUNT(id) FROM consumer_ordertype cod WHERE cod.ordertype_id = odt.id) AS TotalConsumer
                FROM ordertype odt
                OFFSET ?1 ROWS FETCH NEXT ?2 ROWS ONLY 
                   """,
                 nativeQuery = true)
    public List<OrderTypeDashboard> ListManageOrderType(
      Integer offset, Integer limit
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
