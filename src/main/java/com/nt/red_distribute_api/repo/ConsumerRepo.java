package com.nt.red_distribute_api.repo;


import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nt.red_distribute_api.entity.ConsumerEntity;
import com.nt.red_distribute_api.entity.view.consumer.ListConsumerTopic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepo extends JpaRepository<ConsumerEntity,Long> {

    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM consumer WHERE id=?1", nativeQuery = true)
    public ConsumerEntity GetDetail(Long consumerID);

    @Query(value = """
                  SELECT 
                      con.id, con.system_name, con.contactname, con.email, con.phonenumber, 
                      (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.id = cod.consumer_id) AS TotalTopic
                  FROM 
                      consumer_ordertype cod
                  LEFT JOIN 
                      consumer con ON con.id = cod.consumer_id
                  WHERE cod.ordertype_id = :ordertype_id 
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
                      consumer con ON con.id = cod.consumer_id
                  WHERE cod.ordertype_id = :ordertype_id 
                   """,
                 nativeQuery = true)
    public Integer getListConsumerOrderTypeTotal(
      @Param(value = "ordertype_id") Long orderTypeID
    );

    @Query(value = """
        SELECT 
            con.id, con.system_name, con.contactname, con.email, con.phonenumber,
            (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.id = cod.consumer_id) AS TotalTopic
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.id = cod.consumer_id
        WHERE cod.ordertype_id = :ordertype_id 
        AND ( con.system_name like %:search% OR con.contactname like  %:search% OR con.email like  %:search% OR con.phonenumber like  %:search% )
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
            consumer con ON con.id = cod.consumer_id
        WHERE cod.ordertype_id = :ordertype_id 
        AND ( con.system_name like %:search% OR con.contactname like  %:search% OR con.email like  %:search% OR con.phonenumber like  %:search% )
        """,
        nativeQuery = true)
    public Integer getListConsumerOrderTypeAllLikeTotal(
      @Param(value = "ordertype_id") Long orderTypeID,
      @Param(value = "search") String search
    );


    // system name
    @Query(value = """
        SELECT 
            con.id, con.system_name, con.contactname, con.email, con.phonenumber,
            (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.id = cod.consumer_id) AS TotalTopic
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.id = cod.consumer_id
        WHERE cod.ordertype_id = :ordertype_id 
        AND ( con.system_name like %:search% )
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
            consumer con ON con.id = cod.consumer_id
        WHERE cod.ordertype_id = :ordertype_id 
        AND ( con.system_name like %:search% )
        """,
        nativeQuery = true)
    public Integer getListConsumerOrderTypeSystemNameLikeTotal(
      @Param(value = "ordertype_id") Long orderTypeID,
      @Param(value = "search") String search
    );


    // contactname
    @Query(value = """
        SELECT 
            con.id, con.system_name, con.contactname, con.email, con.phonenumber,
            (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.id = cod.consumer_id) AS TotalTopic
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.id = cod.consumer_id
        WHERE cod.ordertype_id = :ordertype_id 
        AND ( con.contactname like %:search% )
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
            consumer con ON con.id = cod.consumer_id
        WHERE cod.ordertype_id = :ordertype_id 
        AND ( con.contactname like %:search% )
        """,
        nativeQuery = true)
    public Integer getListConsumerOrderTypeContactNameLikeTotal(
      @Param(value = "ordertype_id") Long orderTypeID,
      @Param(value = "search") String search
    );


    // email
    @Query(value = """
        SELECT 
            con.id, con.system_name, con.contactname, con.email, con.phonenumber,
            (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.id = cod.consumer_id) AS TotalTopic
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.id = cod.consumer_id
        WHERE cod.ordertype_id = :ordertype_id 
        AND ( con.email like %:search% )
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
            consumer con ON con.id = cod.consumer_id
        WHERE cod.ordertype_id = :ordertype_id 
        AND ( con.email like %:search% )
        """,
        nativeQuery = true)
    public Integer getListConsumerOrderTypeEmailLikeTotal(
      @Param(value = "ordertype_id") Long orderTypeID,
      @Param(value = "search") String search
    );

    // phonenumber
    @Query(value = """
        SELECT 
            con.id, con.system_name, con.contactname, con.email, con.phonenumber,
            (SELECT COUNT(id) FROM consumer_ordertype cod WHERE con.id = cod.consumer_id) AS TotalTopic
        FROM 
            consumer_ordertype cod
        LEFT JOIN 
            consumer con ON con.id = cod.consumer_id
        WHERE cod.ordertype_id = :ordertype_id 
        AND ( con.phonenumber like %:search% )
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
            consumer con ON con.id = cod.consumer_id
        WHERE cod.ordertype_id = :ordertype_id 
        AND ( con.phonenumber like %:search% )
        """,
        nativeQuery = true)
    public Integer getListConsumerOrderTypePhoneNumberLikeTotal(
      @Param(value = "ordertype_id") Long orderTypeID,
      @Param(value = "search") String search
    );
}
