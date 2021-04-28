package demo.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {
    String userName;
    String password;
    String phone;
}
