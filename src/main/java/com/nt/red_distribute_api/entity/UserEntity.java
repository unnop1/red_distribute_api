package com.nt.red_distribute_api.entity;


import java.sql.Timestamp;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table (name = "USER_DB")
public class UserEntity implements UserDetails {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_db_seq")
        @SequenceGenerator(name = "user_db_seq", allocationSize = 1)
        private long id;

        @Column(name = "NAME", unique = false,nullable = true)
        private String name=null;

        @Column(name = "EMAIL", unique = false,nullable = true)
        private String email=null;

        @Column(name = "USERNAME", unique = false,nullable = true)
        private String username=null;

        @Column(name = "PASSWORD", unique = false,nullable = true)
        private String password=null;

        @Column(name = "PHONENUMBER", unique = false,nullable = true)
        private String phoneNumber=null;

        @Column(name = "CURRENTTOKEN", unique = true,nullable = true)
        private String currentToken=null;

        @Column(name = "ABOUT_ME", unique = false,nullable = true)
        private String about_me=null;

        @Column(name = "LAST_LOGIN", unique = false,nullable = true)
        private Timestamp last_login=null;
        
        @Column(name = "LAST_LOGIN_IPADDRESS", unique = false,nullable = true)
        private String last_login_ipaddress=null;

        @Column(name = "IS_ENABLE", unique = false,nullable = true)
        private Integer is_Enable=1;

        @Column(name = "IS_DELETE", unique = false,nullable = true)
        private Integer is_Delete=0;

        @Column(name = "IS_DELETE_BY", unique = false,nullable = true)
        private String is_Delete_by=null;

        @Column(name = "IS_DELETE_DATE", unique = false,nullable = true)
        private Timestamp is_Delete_date;

        @Column(name = "CREATED_DATE", unique = false,nullable = true)
        private Timestamp created_Date;

        @Column(name = "CREATED_BY", unique = false,nullable = true)
        private String created_by=null;

        @Column(name = "UPDATED_DATE", unique = false,nullable = true)
        private Timestamp updated_Date=null;

        @Column(name = "UPDATED_BY", unique = false,nullable = true)
        private String updated_by=null;

        @Column(name = "SA_MENU_PERMISSION_ID", unique = false,nullable = true)
        private Long sa_menu_permission_id=null;

        @Column(name = "DEPARTMENTNAME", unique = false,nullable = true)
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

