package com.example.demo.Util;

import com.example.demo.eneity.User;
import org.springframework.stereotype.Component;

@Component
@Deprecated
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
