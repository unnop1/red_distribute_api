package com.nt.red_distribute_api.repo;


import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nt.red_distribute_api.entity.ConsumerOrderTypeEntity;
import com.nt.red_distribute_api.entity.view.consumer_ordertype.ConsumerLJoinOrderType;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ConsumerOrderTypeRepo extends JpaRepository<ConsumerOrderTypeEntity,Long> {

    @Query(value = """
                  SELECT * FROM consumer_ordertype WHERE ID=?1  
                   """,
                 nativeQuery = true)
    public ConsumerOrderTypeEntity getConsumerOrderTypeByID(
      Long codID
    );

    @Query(value = """
                  SELECT cod.ID, cod.CONSUMER_ID,odt.ORDERTYPE_NAME, cod.ORDERTYPE_ID
                  FROM consumer_ordertype cod
                  LEFT join ordertype odt
                  ON cod.ORDERTYPE_ID = odt.ID
                  WHERE cod.ORDERTYPE_ID=?1
                   """,
                 nativeQuery = true)
    public ConsumerLJoinOrderType getConsumerOrderTypeByOrderTypeID(
      Long orderTypeID
    );

    @Query(value = """
                  SELECT cod.ID, cod.CONSUMER_ID,odt.ORDERTYPE_NAME, cod.ORDERTYPE_ID
                  FROM consumer_ordertype cod
                  LEFT join ordertype odt
                  ON cod.ORDERTYPE_ID = odt.ID
                  WHERE cod.CONSUMER_ID=?1 AND cod.ORDERTYPE_ID=?2
                   """,
                 nativeQuery = true)
    public ConsumerLJoinOrderType getOneConsumerOrderTypeUnique(
      Long consumerID, Long orderTypeID
    );

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

    @Modifying
    @Transactional
    @Query(value = """
                      DELETE FROM consumer_ordertype cod
                      WHERE cod.CONSUMER_ID=:consumer_id
                   """,
           nativeQuery = true)
    public void deleteConsumerOrderTypeByConsumerID(@Param(value = "consumer_id") Long consumerID);

    @Modifying
    @Transactional
    @Query(value = """
                      DELETE FROM consumer_ordertype cod
                      WHERE cod.ORDERTYPE_ID=:ordertype_id
                   """,
           nativeQuery = true)
    public void deleteConsumerOrderTypeByOrderTypeID(@Param(value = "ordertype_id") Long orderTypeID);
    

}
