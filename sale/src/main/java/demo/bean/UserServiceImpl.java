package demo.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserDao userDao;

    @Override
    public User create(String username, String password) {
        User user = new User();
        String hash = encoder.encode(password);
        user.setUsername(username);
        user.setPassword(hash);
        return userDao.save(user);
    }
}
