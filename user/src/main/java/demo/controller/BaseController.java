package demo.controller;

import demo.bean.Account;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseController {
    public Account getCurrentAccount() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (Account) principal;
    }
}
