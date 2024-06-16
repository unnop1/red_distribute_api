package com.nt.red_distribute_api.repo;


import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.nt.red_distribute_api.entity.ConsumerOrderTypeEntity;
import com.nt.red_distribute_api.entity.view.consumer_ordertype.ConsumerLJoinOrderType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerOrderTypeRepo extends JpaRepository<ConsumerOrderTypeEntity,Long> {

    @Query(value = """
                  SELECT cod.CONSUMER_ID,odt.ORDERTYPE_NAME, odt.ORDERTYPE_ID
                  FROM consumer_ordertype cod
                  LEFT join ordertype odt
                  ON cod.ORDERTYPE_ID = odt.ID
                  WHERE cod.CONSUMER_ID=?1
                   """,
                 nativeQuery = true)
    public List<ConsumerLJoinOrderType> ConsumerOrderTypeName(
      Long consumerID
    );

    

}
