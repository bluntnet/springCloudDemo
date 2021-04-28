package demo.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Account {
    String name;
    String password;
    String phone;
}
