package com.nt.red_distribute_api.repo;


import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.nt.red_distribute_api.entity.ConsumerOrderTypeEntity;
import com.nt.red_distribute_api.entity.OrderTypeEntity;
import com.nt.red_distribute_api.entity.view.consumer_ordertype.ConsumerLJoinOrderType;
import com.nt.red_distribute_api.entity.view.order_type.OrderTypeDashboardTrigger;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerOrderTypeRepo extends JpaRepository<ConsumerOrderTypeEntity,Long> {

    @Query(value = """
                  SELECT cod.consumer_id,odt.ordertype_name
                  FROM consumer_ordertype cod
                  LEFT join ordertype odt
                  ON cod.ordertype_id = odt.id
                  WHERE cod.consumer_id=?1
                   """,
                 nativeQuery = true)
    public List<ConsumerLJoinOrderType> ConsumerOrderTypeName(
      Long consumerID
    );

    

}
