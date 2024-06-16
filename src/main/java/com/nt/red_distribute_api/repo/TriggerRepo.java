package com.nt.red_distribute_api.repo;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nt.red_distribute_api.entity.TriggerMessageEntity;
import com.nt.red_distribute_api.entity.view.trigger.DashboardTrigger;

import java.sql.Timestamp;
public interface TriggerRepo extends JpaRepository<TriggerMessageEntity,Long> {

    @Query(value = "SELECT * FROM trigger_message WHERE ID=?1 ", nativeQuery = true)
    public TriggerMessageEntity findTriggerById(Long triggerID);

    /* WITH TIME */
    @Query(value =  """
                    SELECT trg.ID,trg.ORDERID,trg.PHONENUMBER, trg.ORDERTYPE_NAME,trg.PUBLISH_CHANNEL,trg.RECEIVE_DATE,trg.SEND_DATE 
                    FROM trigger_message trg 
                    WHERE trg.ORDERTYPE_ID=:order_type_id
                    AND (trg.RECEIVE_DATE >= :start_time) 
                    AND (trg.RECEIVE_DATE <= :end_time) 
                    """
                    ,nativeQuery = true)
    public List<DashboardTrigger> ListTriggerWithTime(
                                            @Param(value = "order_type_id")Long orderTypeID,
                                            @Param(value = "start_time") Timestamp startTime,
                                            @Param(value = "end_time")Timestamp endTime,
                                            Pageable pageable);

    @Query(value = """
                    SELECT COUNT(*)
                    FROM trigger_message trg 
                    WHERE trg.ORDERTYPE_ID=:order_type_id
                    AND (trg.RECEIVE_DATE >= :start_time) 
                    AND (trg.RECEIVE_DATE <= :end_time) 
                    """,
                    nativeQuery = true)
    public Integer getListTriggerWithTimeTotalCount(@Param(value = "order_type_id")Long orderTypeID, @Param(value = "start_time") Timestamp startTime,
    @Param(value = "end_time")Timestamp endTime);

    @Query(value =  """
                    SELECT trg.ID,trg.ORDERID,trg.PHONENUMBER, trg.ORDERTYPE_NAME,trg.PUBLISH_CHANNEL,trg.RECEIVE_DATE,trg.SEND_DATE
                    FROM trigger_message trg 
                    WHERE trg.ORDERTYPE_ID=:order_type_id
                    AND (trg.RECEIVE_DATE >= :start_time) 
                    AND (trg.RECEIVE_DATE <= :end_time) 
                    AND ( trg.ORDERID like %:search% OR trg.PHONENUMBER like %:search% OR trg.ORDERTYPE_NAME like %:search% OR trg.PUBLISH_CHANNEL like %:search% ) 
                    """,
                    nativeQuery = true)
    public List<DashboardTrigger> ListTriggerWithTimeAllLike(
        @Param(value = "order_type_id")Long orderTypeID,
        @Param(value = "start_time") Timestamp startTime,
                                            @Param(value = "end_time")Timestamp endTime,
                                            @Param(value = "search") String search,
                                            Pageable pageable);

    @Query(value = """
        SELECT COUNT(*)
        FROM trigger_message trg 
        WHERE trg.ORDERTYPE_ID=:order_type_id
        AND (trg.RECEIVE_DATE >= :start_time) 
        AND (trg.RECEIVE_DATE <= :end_time) 
        AND ( trg.ORDERID like %:search% OR trg.PHONENUMBER like %:search% OR trg.ORDERTYPE_NAME like %:search% OR trg.PUBLISH_CHANNEL like %:search% ) 
        """,
        nativeQuery = true
    )
    public Integer getListTriggerWithTimeAllLikeTotalCount(
        @Param(value = "order_type_id")Long orderTypeID,    
        @Param(value = "start_time") Timestamp startTime,
        @Param(value = "end_time")Timestamp endTime,
        @Param(value = "search") String search);

    // ORDERID
    @Query(value =  """
                    SELECT trg.ID,trg.ORDERID,trg.PHONENUMBER, trg.ORDERTYPE_NAME,trg.PUBLISH_CHANNEL,trg.RECEIVE_DATE,trg.SEND_DATE
                    FROM trigger_message trg 
                    WHERE trg.ORDERTYPE_ID=:order_type_id
                    AND (trg.RECEIVE_DATE >= :start_time) 
                    AND (trg.RECEIVE_DATE <= :end_time) 
                    AND ( trg.ORDERID like %:search% ) 
                    """,
                    nativeQuery = true
    )
    public List<DashboardTrigger> ListOrderIDWithTimeLike(
                                            @Param(value = "order_type_id")Long orderTypeID,
                                            @Param(value = "start_time") Timestamp startTime,
                                            @Param(value = "end_time")Timestamp endTime,
                                            @Param(value = "search") String search,
                                            Pageable pageable);

    @Query(value = """
                    SELECT COUNT(*)
                    FROM trigger_message trg 
                    WHERE trg.ORDERTYPE_ID=:order_type_id
                    AND (trg.RECEIVE_DATE >= :start_time) 
                    AND (trg.RECEIVE_DATE <= :end_time) 
                    AND ( trg.ORDERID like %:search% ) 
                    """,
                    nativeQuery = true)
    public Integer getListOrderIDWithTimeLikeTotalCount(
                                                        @Param(value = "order_type_id")Long orderTypeID,
                                                        @Param(value = "start_time") Timestamp startTime,
                                                        @Param(value = "end_time")Timestamp endTime,
                                                        @Param(value = "search") String search);

                                                        
    // phonenumber
    @Query(value =  """
                    SELECT trg.ID,trg.ORDERID,trg.PHONENUMBER, trg.ORDERTYPE_NAME,trg.PUBLISH_CHANNEL,trg.RECEIVE_DATE,trg.SEND_DATE
                    FROM trigger_message trg 
                    WHERE trg.ORDERTYPE_ID=:order_type_id
                    AND(trg.RECEIVE_DATE >= :start_time) 
                    AND (trg.RECEIVE_DATE <= :end_time) 
                    AND ( trg.PhoneNumber like %:search% ) 
                    """,
                    nativeQuery = true
    )
    public List<DashboardTrigger> ListPhoneNumberWithTimeLike(
                                            @Param(value = "order_type_id")Long orderTypeID,
                                            @Param(value = "start_time") Timestamp startTime,
                                            @Param(value = "end_time")Timestamp endTime,
                                            @Param(value = "search") String search,
                                            Pageable pageable);

    @Query(value = """
                    SELECT COUNT(*)
                    FROM trigger_message trg 
                    WHERE trg.ORDERTYPE_ID=:order_type_id
                    AND (trg.RECEIVE_DATE >= :start_time) 
                    AND (trg.RECEIVE_DATE <= :end_time) 
                    AND ( trg.PhoneNumber like %:search% ) 
                    """,
                    nativeQuery = true)
    public Integer getListPhoneNumberWithTimeLikeTotalCount(
                                                        @Param(value = "order_type_id")Long orderTypeID,    
                                                        @Param(value = "start_time") Timestamp startTime,
                                                        @Param(value = "end_time")Timestamp endTime,
                                                        @Param(value = "search") String search);

    // ORDERTYPE_NAME
    @Query(value =  """
                    SELECT trg.ID,trg.ORDERID,trg.PHONENUMBER, trg.ORDERTYPE_NAME,trg.PUBLISH_CHANNEL,trg.RECEIVE_DATE,trg.SEND_DATE
                    FROM trigger_message trg 
                    WHERE trg.ORDERTYPE_ID=:order_type_id
                    AND (trg.RECEIVE_DATE >= :start_time) 
                    AND (trg.RECEIVE_DATE <= :end_time) 
                    AND ( trg.ORDERTYPE_NAME like %:search% ) 
                    """,
                    nativeQuery = true
    )
    public List<DashboardTrigger> ListORDERTYPENAMEWithTimeLike(
        @Param(value = "order_type_id")Long orderTypeID,    
        @Param(value = "start_time") Timestamp startTime,
        @Param(value = "end_time")Timestamp endTime,
        @Param(value = "search") String search,
        Pageable pageable);

    @Query(value = """
                    SELECT COUNT(*)
                    FROM trigger_message trg 
                    WHERE trg.ORDERTYPE_ID=:order_type_id
                    AND (trg.RECEIVE_DATE >= :start_time) 
                    AND (trg.RECEIVE_DATE <= :end_time) 
                    AND ( trg.ORDERTYPE_NAME like %:search% ) 
                    """,
                    nativeQuery = true)
    public Integer getListORDERTYPENAMEWithTimeLikeTotalCount(
        @Param(value = "order_type_id")Long orderTypeID,
        @Param(value = "start_time") Timestamp startTime,
        @Param(value = "end_time")Timestamp endTime,
        @Param(value = "search") String search);

    // PUBLISH_CHANNEL
    @Query(value =  """
                    SELECT trg.ID,trg.ORDERID,trg.PHONENUMBER, trg.ORDERTYPE_NAME,trg.PUBLISH_CHANNEL,trg.RECEIVE_DATE,trg.SEND_DATE
                    FROM trigger_message trg 
                    WHERE (trg.RECEIVE_DATE >= :start_time) 
                    AND (trg.RECEIVE_DATE <= :end_time) 
                    AND ( trg.PUBLISH_CHANNEL like %:search% ) 
                    """,
                    nativeQuery = true
    )
    public List<DashboardTrigger> ListPUBLISHCHANNELWithTimeLike(@Param(value = "start_time") Timestamp startTime,
                                            @Param(value = "end_time")Timestamp endTime,
                                            @Param(value = "search") String search,
                                            Pageable pageable);

    @Query(value = """
                    SELECT COUNT(*)
                    FROM trigger_message trg 
                    WHERE (trg.RECEIVE_DATE >= :start_time) 
                    AND (trg.RECEIVE_DATE <= :end_time) 
                    AND ( trg.PUBLISH_CHANNEL like %:search% ) 
                    """,
                    nativeQuery = true)
    public Integer getListPUBLISHCHANNELWithTimeLikeTotalCount(@Param(value = "start_time") Timestamp startTime,
                                                        @Param(value = "end_time")Timestamp endTime,
                                                        @Param(value = "search") String search);


    /* WITHOUT TIME */
    @Query(value =  """
                    SELECT trg.ID,trg.ORDERID,trg.PHONENUMBER, trg.ORDERTYPE_NAME,trg.PUBLISH_CHANNEL,trg.RECEIVE_DATE,trg.SEND_DATE
                    FROM trigger_message trg  
                    WHERE trg.ORDERTYPE_ID=:order_type_id
                    """
                    ,nativeQuery = true)
    public List<DashboardTrigger> ListTriggerWITHOUTTime(@Param(value = "order_type_id")Long orderTypeID,Pageable pageable);

    @Query(value = """
                    SELECT COUNT(*)
                    FROM trigger_message trg  
                    WHERE trg.ORDERTYPE_ID=:order_type_id
                    """,
                    nativeQuery = true)
    public Integer getListTriggerWITHOUTTimeTotalCount(@Param(value = "order_type_id")Long orderTypeID);

    @Query(value =  """
                    SELECT trg.ID,trg.ORDERID,trg.PHONENUMBER, trg.ORDERTYPE_NAME,trg.PUBLISH_CHANNEL,trg.RECEIVE_DATE,trg.SEND_DATE
                    FROM trigger_message trg  
                    WHERE trg.ORDERTYPE_ID=:order_type_id 
                    AND ( trg.ORDERID like %:search% OR trg.PHONENUMBER like %:search% OR trg.ORDERTYPE_NAME like %:search% OR trg.PUBLISH_CHANNEL like %:search% ) 
                    """,
                    nativeQuery = true)
    public List<DashboardTrigger> ListTriggerWITHOUTTimeAllLike(
                                            @Param(value = "order_type_id")Long orderTypeID,
                                            @Param(value = "search") String search,
                                            Pageable pageable);

    @Query(value = """
        SELECT COUNT(*)
        FROM trigger_message trg 
        WHERE trg.ORDERTYPE_ID=:order_type_id 
        AND ( trg.ORDERID like %:search% OR trg.PHONENUMBER like %:search% OR trg.ORDERTYPE_NAME like %:search% OR trg.PUBLISH_CHANNEL like %:search% ) 
        """,
        nativeQuery = true
    )
    public Integer getListTriggerWITHOUTTimeAllLikeTotalCount(@Param(value = "order_type_id")Long orderTypeID, @Param(value = "search") String search);

    // ORDERID
    @Query(value =  """
                    SELECT trg.ID,trg.ORDERID,trg.PHONENUMBER, trg.ORDERTYPE_NAME,trg.PUBLISH_CHANNEL,trg.RECEIVE_DATE,trg.SEND_DATE
                    FROM trigger_message trg  
                    WHERE trg.ORDERTYPE_ID=:order_type_id 
                    AND ( trg.ORDERID like %:search% ) 
                    """,
                    nativeQuery = true
    )
    public List<DashboardTrigger> ListOrderIDWITHOUTTimeLike(
                                            @Param(value = "order_type_id")Long orderTypeID,
                                            @Param(value = "search") String search,
                                            Pageable pageable);

    @Query(value = """
                    SELECT COUNT(*)
                    FROM trigger_message trg  
                    WHERE trg.ORDERTYPE_ID=:order_type_id  
                    AND ( trg.ORDERID like %:search% ) 
                    """,
                    nativeQuery = true)
    public Integer getListOrderIDWITHOUTTimeLikeTotalCount(
                                                        @Param(value = "order_type_id")Long orderTypeID,
                                                        @Param(value = "search") String search);

                                                        
    // phonenumber
    @Query(value =  """
                    SELECT trg.ID,trg.ORDERID,trg.PHONENUMBER, trg.ORDERTYPE_NAME,trg.PUBLISH_CHANNEL,trg.RECEIVE_DATE,trg.SEND_DATE
                    FROM trigger_message trg  
                    WHERE trg.ORDERTYPE_ID=:order_type_id 
                    AND ( trg.PhoneNumber like %:search% ) 
                    """,
                    nativeQuery = true
    )
    public List<DashboardTrigger> ListPhoneNumberWITHOUTTimeLike(
                                            @Param(value = "order_type_id")Long orderTypeID,
                                            @Param(value = "search") String search,
                                            Pageable pageable);

    @Query(value = """
                    SELECT COUNT(*)
                    FROM trigger_message trg  
                    WHERE trg.ORDERTYPE_ID=:order_type_id 
                    AND ( trg.PhoneNumber like %:search% ) 
                    """,
                    nativeQuery = true)
    public Integer getListPhoneNumberWITHOUTTimeLikeTotalCount(
                                                        @Param(value = "order_type_id")Long orderTypeID,
                                                        @Param(value = "search") String search);

    // ORDERTYPE_NAME
    @Query(value =  """
                    SELECT trg.ID,trg.ORDERID,trg.PHONENUMBER, trg.ORDERTYPE_NAME,trg.PUBLISH_CHANNEL,trg.RECEIVE_DATE,trg.SEND_DATE
                    FROM trigger_message trg  
                    WHERE trg.ORDERTYPE_ID=:order_type_id 
                    AND ( trg.ORDERTYPE_NAME like %:search% ) 
                    """,
                    nativeQuery = true
    )
    public List<DashboardTrigger> ListORDERTYPENAMEWITHOUTTimeLike(
                                            @Param(value = "order_type_id")Long orderTypeID,
                                            @Param(value = "search") String search,
                                            Pageable pageable);

    @Query(value = """
                    SELECT COUNT(*)
                    FROM trigger_message trg  
                    WHERE trg.ORDERTYPE_ID=:order_type_id 
                    AND ( trg.ORDERTYPE_NAME like %:search% ) 
                    """,
                    nativeQuery = true)
    public Integer getListORDERTYPENAMEWITHOUTTimeLikeTotalCount(
        @Param(value = "order_type_id")Long orderTypeID,
        @Param(value = "search") String search);

    // PUBLISH_CHANNEL
    @Query(value =  """
                    SELECT trg.ID,trg.ORDERID,trg.PHONENUMBER, trg.ORDERTYPE_NAME,trg.PUBLISH_CHANNEL,trg.RECEIVE_DATE,trg.SEND_DATE
                    FROM trigger_message trg  
                    WHERE trg.ORDERTYPE_ID=:order_type_id 
                    AND ( trg.PUBLISH_CHANNEL like %:search% ) 
                    """,
                    nativeQuery = true
    )
    public List<DashboardTrigger> ListPUBLISHCHANNELWITHOUTTimeLike(
        @Param(value = "order_type_id")Long orderTypeID,
        @Param(value = "search") String search,
        Pageable pageable);

    @Query(value = """
                    SELECT COUNT(*)
                    FROM trigger_message trg  
                    WHERE trg.ORDERTYPE_ID=:order_type_id 
                    AND ( trg.PUBLISH_CHANNEL like %:search% ) 
                    """,
                    nativeQuery = true)
    public Integer getListPUBLISHCHANNELWITHOUTTimeLikeTotalCount(
        @Param(value = "order_type_id")Long orderTypeID,
        @Param(value = "search") String search);
    
    
}