package com.nt.red_distribute_api.entity;

import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "consumer_ordertype")
public class ConsumerOrderTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consumer_ordertype_seq")
    @SequenceGenerator(name = "consumer_ordertype_seq", allocationSize = 1)
    @Column(name = "ID")
    private Long ID;

    @Column(name = "consumer_id", nullable = true)
    private String consumer_id=null;

    @Column(name = "ordertype_id", nullable = true)
    private String ordertype_id=null;

    @Column(name = "created_date", nullable = true)
    private Timestamp created_date=null;

    @Column(name = "created_by", nullable = true)
    private String created_by=null;
}
