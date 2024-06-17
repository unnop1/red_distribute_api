package com.nt.red_distribute_api.repo;


import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nt.red_distribute_api.entity.ConsumerOrderTypeEntity;
import com.nt.red_distribute_api.entity.view.consumer_ordertype.ConsumerLJoinOrderType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerOrderTypeRepo extends JpaRepository<ConsumerOrderTypeEntity,Long> {

    @Query(value = """
                  SELECT cod.ID, cod.CONSUMER_ID,odt.ORDERTYPE_NAME, cod.ORDERTYPE_ID
                  FROM consumer_ordertype cod
                  LEFT join ordertype odt
                  ON cod.ORDERTYPE_ID = odt.ID
                  WHERE cod.CONSUMER_ID=?1
                   """,
                 nativeQuery = true)
    public List<ConsumerLJoinOrderType> ConsumerOrderTypeName(
      Long consumerID
    );

    @Query(value = """
                  DELETE FROM consumer_ordertype cod
                  WHERE cod.CONSUMER_ID=:consumer_id
                   """,
                 nativeQuery = true)
    public List<ConsumerLJoinOrderType> deleteConsumerOrderTypeByConsumerID(
      @Param(value = "consumer_id") Long consumerID
    );

    

}
