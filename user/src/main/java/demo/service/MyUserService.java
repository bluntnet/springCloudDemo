package demo.service;

import demo.bean.Account;
import demo.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MyUserService implements UserDetailsService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Account account = accountMapper.loadByName(s);
        if (account == null) {
            throw new RuntimeException("用户不存在" + s);
        }

        log.debug("account . authorize = authorize.size() = "+account.getAuthorities().size());

        /*Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        log.debug("====[load account] = {}", account);

        if (account.getRole() == 0) {
            authorities.add(new SimpleGrantedAuthority(RoleConstant.PRE_ROLE + RoleConstant.ADMIN));
        } else {
            authorities.add(new SimpleGrantedAuthority(RoleConstant.PRE_ROLE + RoleConstant.USER));
        }
        //AuthorityUtils.createAuthorityList(user.getRoles().toArray(new String[]{})
        account.setAuthorities(authorities);*/
        return account;
    }
}
