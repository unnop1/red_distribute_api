package com.nt.red_distribute_api.repo;


import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.nt.red_distribute_api.entity.OrderTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTypeRepo extends JpaRepository<OrderTypeEntity,Long> {

    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM order_type WHERE TYPEID=?1", nativeQuery = true)
    public Optional<OrderTypeEntity> findById(Long orderTypeID);

    @SuppressWarnings("null")
    @Query(value = "SELECT * FROM order_type WHERE MAINID=?1", nativeQuery = true)
    public OrderTypeEntity findByMainId(Long orderTypeMainID);
}
