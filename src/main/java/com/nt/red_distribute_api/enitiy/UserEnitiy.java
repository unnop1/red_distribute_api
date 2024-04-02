package com.nt.red_distribute_api.enitiy;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Getter
@Setter
@Table (name = "user_db")
public class UserEnitiy implements UserDetails {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private long id;

        @Column(name = "name", unique = false,nullable = true)
        private String name;

        @Column(name = "email", unique = true,nullable = false)
        private String email;

        @Column(name = "password", unique = false,nullable = false)
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

        @Column(name = "isDelete", unique = false,nullable = true)
        private Integer isDelete;

        @Column(name = "isDelete_by", unique = false,nullable = true)
        private String isDelete_by;

        @Column(name = "isDelete_date", unique = false,nullable = true)
        private Timestamp isDelete_date;

        @Column(name = "createdDate", unique = false,nullable = true)
        private Timestamp createdDate;

        @Column(name = "created_by", unique = false,nullable = true)
        private String created_by;

        @Column(name = "updatedDate", unique = false,nullable = true)
        private Timestamp updatedDate;

        @Column(name = "updated_by", unique = false,nullable = true)
        private String updated_by;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
        }

        @Override
        public String getUsername() {
                return this.email;
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
