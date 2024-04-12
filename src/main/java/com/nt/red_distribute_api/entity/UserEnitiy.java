package com.nt.red_distribute_api.entity;



import jakarta.persistence.*;
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
public class UserEnitiy implements UserDetails {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_db_seq")
        @SequenceGenerator(name = "user_db_seq", allocationSize = 1)
        private long id;

        @Column(name = "name", unique = false,nullable = true)
        private String name;

        @Column(name = "email", unique = false,nullable = true)
        private String email;

        @Column(name = "username", unique = false,nullable = true)
        private String username;

        @Column(name = "password", unique = false,nullable = true)
        private String password;

        @Column(name = "phoneNumber", unique = false,nullable = true)
        private String phoneNumber;

        @Column(name = "currentToken", unique = true,nullable = true)
        private String currentToken;

        @Column(name = "about_me", unique = false,nullable = true)
        private String aboutMe;

        @Column(name = "last_login", unique = false,nullable = true)
        private Timestamp last_login;
        
        @Column(name = "last_login_ipaddress", unique = false,nullable = true)
        private String last_login_ipaddress;

        @Column(name = "is_Delete", unique = false,nullable = true)
        private Integer isDelete;

        @Column(name = "is_Delete_by", unique = false,nullable = true)
        private String isDelete_by;

        @Column(name = "is_Delete_date", unique = false,nullable = true)
        private Timestamp isDelete_date;

        @Column(name = "created_Date", unique = false,nullable = true)
        private Timestamp createdDate;

        @Column(name = "created_by", unique = false,nullable = true)
        private String created_by;

        @Column(name = "updated_Date", unique = false,nullable = true)
        private Timestamp updatedDate;

        @Column(name = "updated_by", unique = false,nullable = true)
        private String updated_by;

        @Column(name = "sa_menu_permission_id", unique = false,nullable = true)
        private Long sa_permission_id;

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