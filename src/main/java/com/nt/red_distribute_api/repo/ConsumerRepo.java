package com.nt.red_distribute_api.repo;


import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nt.red_distribute_api.entity.ConsumerEntity;
import com.nt.red_distribute_api.entity.view.consumer.ListConsumerTopic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepo extends JpaRepository<ConsumerEntity,Long> {

    @Query(value = """
                  SELECT * 
                  FROM 
                      consumer
                  WHERE USERNAME=?1
                   """,
                 nativeQuery = true)
    public ConsumerEntity ConsumerByUsername(String username);
    

    ////////////////////////////////////////////////////////////////
    //                        All OrderType Id                    //
    ////////////////////////////////////////////////////////////////

    @Query(value = """
                  SELECT 
                      con.ID, con.SYSTEM_NAME, con.CONTACTNAME, con.EMAIL, con.PHONENUMBER, 
                      (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.ID = cod.CONSUMER_ID) AS TotalTopic
                  FROM 
                      consumer_ordertype cod
                  LEFT JOIN 
                      consumer con ON con.ID = cod.CONSUMER_ID
                   """,
                 nativeQuery = true)
    public List<ListConsumerTopic> ListConsumer(
      Pageable pageable
    );

    @Query(value = """
                  SELECT 
                      COUNT(*)
                  FROM 
                      consumer_ordertype cod
                  LEFT JOIN 
                      consumer con ON con.ID = cod.CONSUMER_ID 
                   """,
                 nativeQuery = true)
    public Integer getListConsumerTotal();

    @Query(value = """
        SELECT 
            con.ID, con.SYSTEM_NAME, con.CONTACTNAME, con.EMAIL, con.PHONENUMBER,
            (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.ID = cod.CONSUMER_ID) AS TotalTopic
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE ( con.SYSTEM_NAME like %:search% OR con.CONTACTNAME like  %:search% OR con.EMAIL like  %:search% OR con.PHONENUMBER like  %:search% )
        """,
        nativeQuery = true)
    public List<ListConsumerTopic> ListConsumerAllLike(
      @Param(value = "search") String search,
      Pageable pageable
    );

    @Query(value = """
        SELECT 
            COUNT(*)
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE ( con.SYSTEM_NAME like %:search% OR con.CONTACTNAME like  %:search% OR con.EMAIL like  %:search% OR con.PHONENUMBER like  %:search% )
        """,
        nativeQuery = true)
    public Integer getListConsumerAllLikeTotal(
      @Param(value = "search") String search
    );


    // system name
    @Query(value = """
        SELECT 
            con.ID, con.SYSTEM_NAME, con.CONTACTNAME, con.EMAIL, con.PHONENUMBER,
            (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.ID = cod.CONSUMER_ID) AS TotalTopic
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE ( con.SYSTEM_NAME like %:search% )
        """,
        nativeQuery = true)
    public List<ListConsumerTopic> ListConsumerSystemNameLike(
      @Param(value = "search") String search,
      Pageable pageable
    );

    @Query(value = """
        SELECT 
            COUNT(*)
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE ( con.SYSTEM_NAME like %:search% )
        """,
        nativeQuery = true)
    public Integer getListConsumerSystemNameLikeTotal(
      @Param(value = "search") String search
    );


    // contactname
    @Query(value = """
        SELECT 
            con.ID, con.SYSTEM_NAME, con.CONTACTNAME, con.EMAIL, con.PHONENUMBER,
            (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.ID = cod.CONSUMER_ID) AS TotalTopic
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE ( con.CONTACTNAME like %:search% )
        """,
        nativeQuery = true)
    public List<ListConsumerTopic> ListConsumerContactNameLike(
      @Param(value = "search") String search,
      Pageable pageable
    );

    @Query(value = """
        SELECT 
            COUNT(*)
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE cod.ORDERTYPE_ID = :ordertype_id 
        AND ( con.CONTACTNAME like %:search% )
        """,
        nativeQuery = true)
    public Integer getListConsumerContactNameLikeTotal(
      @Param(value = "search") String search
    );


    // email
    @Query(value = """
        SELECT 
            con.ID, con.SYSTEM_NAME, con.CONTACTNAME, con.EMAIL, con.PHONENUMBER,
            (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.ID = cod.CONSUMER_ID) AS TotalTopic
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE ( con.EMAIL like %:search% )
        """,
        nativeQuery = true)
    public List<ListConsumerTopic> ListConsumerEmailLike(
      @Param(value = "search") String search,
      Pageable pageable
    );

    @Query(value = """
        SELECT 
            COUNT(*)
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE ( con.EMAIL like %:search% )
        """,
        nativeQuery = true)
    public Integer getListConsumerEmailLikeTotal(
      @Param(value = "search") String search
    );

    // phonenumber
    @Query(value = """
        SELECT 
            con.ID, con.SYSTEM_NAME, con.CONTACTNAME, con.EMAIL, con.PHONENUMBER,
            (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.ID = cod.CONSUMER_ID) AS TotalTopic
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE ( con.PHONENUMBER like %:search% )
        """,
        nativeQuery = true)
    public List<ListConsumerTopic> ListConsumerPhoneNumberLike(
      @Param(value = "search") String search,
      Pageable pageable
    );


    @Query(value = """
        SELECT 
            COUNT(*)
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE ( con.PHONENUMBER like %:search% )
        """,
        nativeQuery = true)
    public Integer getListConsumerPhoneNumberLikeTotal(
      @Param(value = "search") String search
    );

    
    ////////////////////////////////////////////////////////////////
    //                      By OrderType Id                       //
    ////////////////////////////////////////////////////////////////
    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM consumer WHERE id=?1", nativeQuery = true)
    public ConsumerEntity GetDetail(Long consumerID);

    @Query(value = """
                  SELECT 
                      con.ID, con.SYSTEM_NAME, con.CONTACTNAME, con.EMAIL, con.PHONENUMBER, 
                      (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.ID = cod.CONSUMER_ID) AS TotalTopic
                  FROM 
                      consumer_ordertype cod
                  LEFT JOIN 
                      consumer con ON con.ID = cod.CONSUMER_ID
                  WHERE cod.ORDERTYPE_ID = :ordertype_id 
                   """,
                 nativeQuery = true)
    public List<ListConsumerTopic> ListConsumerOrderType(
      @Param(value = "ordertype_id") Long orderTypeID,
      Pageable pageable
    );

    @Query(value = """
                  SELECT 
                      COUNT(*)
                  FROM 
                      consumer_ordertype cod
                  LEFT JOIN 
                      consumer con ON con.ID = cod.CONSUMER_ID
                  WHERE cod.ORDERTYPE_ID = :ordertype_id 
                   """,
                 nativeQuery = true)
    public Integer getListConsumerOrderTypeTotal(
      @Param(value = "ordertype_id") Long orderTypeID
    );

    @Query(value = """
        SELECT 
            con.ID, con.SYSTEM_NAME, con.CONTACTNAME, con.EMAIL, con.PHONENUMBER,
            (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.ID = cod.CONSUMER_ID) AS TotalTopic
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE cod.ORDERTYPE_ID = :ordertype_id 
        AND ( con.SYSTEM_NAME like %:search% OR con.CONTACTNAME like  %:search% OR con.EMAIL like  %:search% OR con.PHONENUMBER like  %:search% )
        """,
        nativeQuery = true)
    public List<ListConsumerTopic> ListConsumerOrderTypeAllLike(
      @Param(value = "ordertype_id") Long orderTypeID,
      @Param(value = "search") String search,
      Pageable pageable
    );

    @Query(value = """
        SELECT 
            COUNT(*)
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE cod.ORDERTYPE_ID = :ordertype_id 
        AND ( con.SYSTEM_NAME like %:search% OR con.CONTACTNAME like  %:search% OR con.EMAIL like  %:search% OR con.PHONENUMBER like  %:search% )
        """,
        nativeQuery = true)
    public Integer getListConsumerOrderTypeAllLikeTotal(
      @Param(value = "ordertype_id") Long orderTypeID,
      @Param(value = "search") String search
    );


    // system name
    @Query(value = """
        SELECT 
            con.ID, con.SYSTEM_NAME, con.CONTACTNAME, con.EMAIL, con.PHONENUMBER,
            (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.ID = cod.CONSUMER_ID) AS TotalTopic
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE cod.ORDERTYPE_ID = :ordertype_id 
        AND ( con.SYSTEM_NAME like %:search% )
        """,
        nativeQuery = true)
    public List<ListConsumerTopic> ListConsumerOrderTypeSystemNameLike(
      @Param(value = "ordertype_id") Long orderTypeID,
      @Param(value = "search") String search,
      Pageable pageable
    );

    @Query(value = """
        SELECT 
            COUNT(*)
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE cod.ORDERTYPE_ID = :ordertype_id 
        AND ( con.SYSTEM_NAME like %:search% )
        """,
        nativeQuery = true)
    public Integer getListConsumerOrderTypeSystemNameLikeTotal(
      @Param(value = "ordertype_id") Long orderTypeID,
      @Param(value = "search") String search
    );


    // contactname
    @Query(value = """
        SELECT 
            con.ID, con.SYSTEM_NAME, con.CONTACTNAME, con.EMAIL, con.PHONENUMBER,
            (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.ID = cod.CONSUMER_ID) AS TotalTopic
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE cod.ORDERTYPE_ID = :ordertype_id 
        AND ( con.CONTACTNAME like %:search% )
        """,
        nativeQuery = true)
    public List<ListConsumerTopic> ListConsumerOrderTypeContactNameLike(
      @Param(value = "ordertype_id") Long orderTypeID,
      @Param(value = "search") String search,
      Pageable pageable
    );

    @Query(value = """
        SELECT 
            COUNT(*)
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE cod.ORDERTYPE_ID = :ordertype_id 
        AND ( con.CONTACTNAME like %:search% )
        """,
        nativeQuery = true)
    public Integer getListConsumerOrderTypeContactNameLikeTotal(
      @Param(value = "ordertype_id") Long orderTypeID,
      @Param(value = "search") String search
    );


    // email
    @Query(value = """
        SELECT 
            con.ID, con.SYSTEM_NAME, con.CONTACTNAME, con.EMAIL, con.PHONENUMBER,
            (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.ID = cod.CONSUMER_ID) AS TotalTopic
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE cod.ORDERTYPE_ID = :ordertype_id 
        AND ( con.EMAIL like %:search% )
        """,
        nativeQuery = true)
    public List<ListConsumerTopic> ListConsumerOrderTypeEmailLike(
      @Param(value = "ordertype_id") Long orderTypeID,
      @Param(value = "search") String search,
      Pageable pageable
    );

    @Query(value = """
        SELECT 
            COUNT(*)
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE cod.ORDERTYPE_ID = :ordertype_id 
        AND ( con.EMAIL like %:search% )
        """,
        nativeQuery = true)
    public Integer getListConsumerOrderTypeEmailLikeTotal(
      @Param(value = "ordertype_id") Long orderTypeID,
      @Param(value = "search") String search
    );

    // phonenumber
    @Query(value = """
        SELECT 
            con.ID, con.SYSTEM_NAME, con.CONTACTNAME, con.EMAIL, con.PHONENUMBER,
            (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.ID = cod.CONSUMER_ID) AS TotalTopic
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE cod.ORDERTYPE_ID = :ordertype_id 
        AND ( con.PHONENUMBER like %:search% )
        """,
        nativeQuery = true)
    public List<ListConsumerTopic> ListConsumerOrderTypePhoneNumberLike(
      @Param(value = "ordertype_id") Long orderTypeID,
      @Param(value = "search") String search,
      Pageable pageable
    );


    @Query(value = """
        SELECT 
            COUNT(*)
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.ID = cod.CONSUMER_ID
        WHERE cod.ORDERTYPE_ID = :ordertype_id 
        AND ( con.PHONENUMBER like %:search% )
        """,
        nativeQuery = true)
    public Integer getListConsumerOrderTypePhoneNumberLikeTotal(
      @Param(value = "ordertype_id") Long orderTypeID,
      @Param(value = "search") String search
    );
}
