package demo.bean;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@Data
@ToString
public class Role implements GrantedAuthority {
    Long id;
    String authority;
}
