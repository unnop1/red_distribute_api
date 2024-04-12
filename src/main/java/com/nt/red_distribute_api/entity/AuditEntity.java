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
@Table (name = "audit")
public class AuditEntity {
           
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_seq")
        @SequenceGenerator(name = "audit_seq", allocationSize = 1)
        @Column(name = "AuditID")
        private Long AuditID;

        @Column(name = "Auditable_ID", unique = false,nullable = false)
        private Long Auditable_ID;

        @Column(name = "Auditable_Type", unique = false,nullable = false)
        private String Auditable_Type;

        @Column(name = "Version", unique = false,nullable = false)
        private String Version;

        @Column(name = "Username", unique = false,nullable = true)
        private String Username = null;

        @Column(name = "Comment", unique = false,nullable = true)
        private String Comment = null;

        @Column(name = "IP", unique = false,nullable = true)
        private String IP = null;

        @Column(name = "Action", unique = false,nullable = false)
        private String Action;

        @Column(name = "CreatedAt", unique = false,nullable = true)
        private Timestamp CreatedAt = null;

        @Column(name = "Created_By_ID", unique = false,nullable = true)
        private Long Created_By_ID = null;






}
