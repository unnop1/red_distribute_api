package com.nt.red_distribute_api.entity;


import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;


@Entity
@Getter
@Setter
@Table (name = "USER_DB")
public class UserEntity implements UserDetails {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_db_seq")
        @SequenceGenerator(name = "user_db_seq", allocationSize = 1)
        private long id;

        @Column(name = "name", unique = false,nullable = true)
        private String name=null;

        @Column(name = "email", unique = false,nullable = true)
        private String email=null;

        @Column(name = "username", unique = false,nullable = true)
        private String username=null;

        @Column(name = "password", unique = false,nullable = true)
        private String password=null;

        @Column(name = "phoneNumber", unique = false,nullable = true)
        private String phoneNumber=null;

        @Column(name = "currentToken", unique = true,nullable = true)
        private String currentToken=null;

        @Column(name = "about_me", unique = false,nullable = true)
        private String about_me=null;

        @Column(name = "last_login", unique = false,nullable = true)
        private Timestamp last_login=null;
        
        @Column(name = "last_login_ipaddress", unique = false,nullable = true)
        private String last_login_ipaddress=null;

        @Column(name = "is_Enable", unique = false,nullable = true)
        private Integer is_Enable=1;

        @Column(name = "is_Delete", unique = false,nullable = true)
        private Integer is_Delete=0;

        @Column(name = "is_Delete_by", unique = false,nullable = true)
        private String is_Delete_by=null;

        @Column(name = "is_Delete_date", unique = false,nullable = true)
        private Timestamp is_Delete_date;

        @Column(name = "created_Date", unique = false,nullable = true)
        private Timestamp created_Date;

        @Column(name = "created_by", unique = false,nullable = true)
        private String created_by=null;

        @Column(name = "updated_Date", unique = false,nullable = true)
        private Timestamp updated_Date=null;

        @Column(name = "updated_by", unique = false,nullable = true)
        private String updated_by=null;

        @Column(name = "sa_menu_permission_id", unique = false,nullable = true)
        private Long sa_menu_permission_id=null;

        @Column(name = "departmentname", unique = false,nullable = true)
        private String departmentname=null;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
        }

        @Override
        public boolean isAccountNonExpired() {
                return true;
        }

        @Override
        public boolean isAccountNonLocked() {
                return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
                return true;
        }

        @Override
        public boolean isEnabled() {
                return true;
        }
}

