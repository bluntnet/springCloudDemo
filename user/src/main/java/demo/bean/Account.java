package demo.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Data
@ToString
public class Account implements UserDetails {
    Long id;
    String username;
    String password;
    String phone;
    Set<Role> authorities;


    @JsonIgnore
    public Set<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    public void setAuthorities(Set<? extends GrantedAuthority> authorities) {
        this.authorities = (Set<Role>) authorities;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
