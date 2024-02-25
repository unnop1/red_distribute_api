package com.nt.red_distribute_api.enitiy;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
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

        @Column(name = "currentToken", unique = true,nullable = true)
        private String currentToken;

        @Column(name = "about_me", unique = false,nullable = true)
        private String aboutMe;

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
