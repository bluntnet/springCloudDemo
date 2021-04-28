package demo.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Account {
    Long id;
    String name;
    String password;
    String phone;
    int role;
}
